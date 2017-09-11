package es.tenerife.secretaria.libro.optimistic;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OptimisticLockException;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.pretty.MessageHelper;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.ReflectionUtils;

import es.tenerife.secretaria.libro.optimistic.ApplicationContextProvider.AppContext;

public class OptimisticLockChecker {

	private static final String STRING_NAME = "name";
	private static final String STRING_ID = "id";

	@PreUpdate
	@PreRemove
	public void preUpdate(Object entity) {
		if (entity instanceof VersionedEntity) {
			check((VersionedEntity) entity);
		}
	}

	private void check(VersionedEntity entity) {
		Long attemptVersion = entity.getOptLock();
		if (attemptVersion == null) {
			throw new NullPointerException("Submitted entity must have a version");
		}

		Class<?> entityClass = entity.getClass();

		// Access to Table Name
		String tableName = getTableName(entityClass);

		Field[] fields = entity.getClass().getDeclaredFields();
		Field idField = Arrays.asList(fields).stream().filter(f -> f.isAnnotationPresent(OptLockId.class)).findFirst()
				.orElse(ReflectionUtils.findField(entityClass, STRING_ID));
		String idColName = getColumnNameFromField(idField);

		Long latestVersion = getLatestVersion(tableName, idColName, entity.getId());

		if (latestVersion != null && !attemptVersion.equals(latestVersion)) {
			throw new OptimisticLockException("Newer version [" + latestVersion + "] of entity ["
					+ MessageHelper.infoString(entityClass.getName(), entity.getId()) + "] found in database");
		}
	}

	private String getColumnNameFromField(Field idField) {
		Column idColAnnotation = idField.getAnnotation(Column.class);
		if (idColAnnotation == null) {
			return idField.getName();
		}

		String idColName = (String) AnnotationUtils.getValue(idColAnnotation, STRING_NAME);
		return StringUtils.isEmpty(idColName) ? idField.getName() : idColName;
	}

	private JdbcTemplate getJdbcTemplate() {
		return AppContext.getApplicationContext().getBean(JdbcTemplate.class);
	}

	private Long getLatestVersion(String tableName, String idColName, Object id) {
		String sql = "select opt_lock from $TABLE_NAME where $ID_COL_NAME = ?";
		sql = sql.replace("$TABLE_NAME", tableName).replace("$ID_COL_NAME", idColName);

		List<Long> resultList = getJdbcTemplate().query(sql, new Object[] { id }, new RowMapper<Long>() {

			@Override
			public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getLong(1);
			}
		});

		if (resultList.isEmpty()) {
			return null;
		} else if (resultList.size() == 1) {
			return resultList.get(0);
		} else {
			throw new EmptyResultDataAccessException(1);
		}
	}

	private String getTableName(Class<?> entityClass) {
		Table tableAnnonation = AnnotationUtils.findAnnotation(entityClass, Table.class);

		Inheritance inheritanceAnnotation = AnnotationUtils.findAnnotation(entityClass,
				javax.persistence.Inheritance.class);
		if (inheritanceAnnotation != null
				&& InheritanceType.JOINED.equals(getInheritanceStrategy(inheritanceAnnotation))) {
			tableAnnonation = AnnotationUtils.findAnnotation(entityClass, Table.class);
			Class<?> superClass = entityClass.getSuperclass();
			while (superClass != null) {
				Table findAnnotation = AnnotationUtils.findAnnotation(superClass, Table.class);
				if (findAnnotation != null) {
					tableAnnonation = findAnnotation;
				}
				superClass = superClass.getSuperclass();
			}
		}

		return (String) AnnotationUtils.getValue(tableAnnonation, STRING_NAME);
	}

	private InheritanceType getInheritanceStrategy(Inheritance inheritanceAnnotation) {
		Object strategyValue = AnnotationUtils.getValue(inheritanceAnnotation, "strategy");
		if (strategyValue != null) {
			return (InheritanceType) strategyValue;
		}
		return InheritanceType.JOINED;
	}
}