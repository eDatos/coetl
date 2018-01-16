package com.arte.application.template.service.criteria;

import java.util.stream.Collectors;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.application.template.domain.Usuario;
import com.arte.application.template.web.rest.errors.CustomParameterizedException;
import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

public class UsuarioCriteriaProcessor extends AbstractCriteriaProcessor {

	private static final String ENTITY_FIELD_LOGIN = "login";
	private static final String ENTITY_FIELD_NOMBRE = "nombre";
	private static final String ENTITY_FIELD_APELLIDO1 = "apellido1";
	private static final String ENTITY_FIELD_APELLIDO2 = "apellido2";
	private static final String ENTITY_FIELD_EMAIL = "email";

	public UsuarioCriteriaProcessor() {
		super(Usuario.class);
	}

	public enum QueryProperty {
		NOMBRE, APELLIDO1, APELLIDO2, LOGIN, ROL, EMAIL
	}

	@Override
	public void registerProcessors() {
		//@formatter:off
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.LOGIN).sortable()
                .withEntityProperty(ENTITY_FIELD_LOGIN).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.NOMBRE).sortable()
    			.withEntityProperty(ENTITY_FIELD_NOMBRE).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.APELLIDO1).sortable()
    			.withEntityProperty(ENTITY_FIELD_APELLIDO1).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.APELLIDO2).sortable()
    			.withEntityProperty(ENTITY_FIELD_APELLIDO2).build());
    	
    	registerProcessorsWithLogicalDeletionPolicy(
                RestrictionProcessorBuilder.restrictionProcessor()
                .withQueryProperty(QueryProperty.ROL)
                .withCriterionConverter(new SqlCriterionBuilder())
                .build());
    	
    	registerOrderProcessor(
	            OrderProcessorBuilder.orderProcessor()
	                .withQueryProperty(QueryProperty.EMAIL)
	                .withEntityProperty(ENTITY_FIELD_EMAIL)
	            .build());

        //@formatter:on
	}

	private static class SqlCriterionBuilder implements CriterionConverter {

		@Override
		public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
			if (QueryProperty.ROL.name().equalsIgnoreCase(property.getLeftExpression())) {
				return usuariosConRoles(property);
			}
			throw new CustomParameterizedException(String.format("Query param not supported: '%s", property));
		}

		private Criterion usuariosConRoles(QueryPropertyRestriction property) {
			// @formatter:off
	        	String sql = String.format(
	        			"{alias}.id in (" +
	        			"SELECT" + 
	        			"	U.ID " + 
	        			"FROM " + 
	        			"	USUARIO U, " + 
	        			"	USUARIO_ROL UR, " + 
	        			"	ROL R " + 
	        			"WHERE " + 
	        			"	U.ID = UR.USUARIO_ID " + 
	        			"	AND R.ID = UR.ROL_ID " + 
	        			"	AND (R.ID " + property.getOperationType() + " (%s) )" +
	        			")", 
	        			property.getRightExpressions().stream().collect(Collectors.joining(",")));
	            // @formatter:on
			return Restrictions.sqlRestriction(sql);
		}
	}

}
