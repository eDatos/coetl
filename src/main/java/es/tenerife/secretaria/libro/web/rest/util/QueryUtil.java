package es.tenerife.secretaria.libro.web.rest.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Component;

import com.arte.libs.grammar.antlr.DefaultQueryExprVisitor;
import com.arte.libs.grammar.antlr.QueryExprCompiler;
import com.arte.libs.grammar.domain.QueryRequest;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;

import es.tenerife.secretaria.libro.service.criteria.OperacionCriteriaProcessor;
import es.tenerife.secretaria.libro.service.criteria.RolCriteriaProcessor;
import es.tenerife.secretaria.libro.service.criteria.UsuarioCriteriaProcessor;

@Component
public class QueryUtil {

	private static final Logger logger = LoggerFactory.getLogger(QueryUtil.class);
	private static final String INCLUDE_DELETED_HINT = "HINT INCLUDE_DELETED SET 'true'";
	private QueryExprCompiler queryExprCompiler = new QueryExprCompiler();

	public DetachedCriteria queryToUserCriteria(String query) {
		return queryToCriteria(query, new UsuarioCriteriaProcessor());
	}

	public DetachedCriteria queryToOperacionCriteria(String query) {
		return queryToCriteria(query, new OperacionCriteriaProcessor());
	}

	public DetachedCriteria queryToRolCriteria(String query) {
		return queryToCriteria(query, new RolCriteriaProcessor());
	}

	public String queryIncludingDeleted(String query) {
		return new StringBuilder(query).append(" ").append(INCLUDE_DELETED_HINT).toString();
	}

	public String pageableSortToQueryString(Pageable pageable) {
		if (pageable == null || pageable.getSort() == null) {
			return StringUtils.EMPTY;
		}

		StringBuilder result = new StringBuilder();
		for (Order pageableOrder : pageable.getSort()) {
			if (!"ID".equalsIgnoreCase(pageableOrder.getProperty())) {
				if (result.length() == 0) {
					result.append(" ORDER BY ");
				} else {
					result.append(", ");
				}
				result.append(pageableOrder.getProperty().toUpperCase());
				if (pageableOrder.isAscending()) {
					result.append(" ").append("ASC");
				} else {
					result.append(" ").append("DESC");
				}
			}
		}
		return result.toString();
	}

	private DetachedCriteria queryToCriteria(String query, AbstractCriteriaProcessor processor) {
		QueryRequest queryRequest = null;
		logger.debug("Petición para mapear query: {}", query);
		if (StringUtils.isNotBlank(query)) {
			DefaultQueryExprVisitor visitor = new DefaultQueryExprVisitor();
			queryExprCompiler.parse(query, visitor);
			queryRequest = visitor.getQueryRequest();
		}
		return processor.process(queryRequest);
	}

}
