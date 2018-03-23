package com.arte.application.template.service.criteria;

import com.arte.application.template.domain.Rol;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

public class RolCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String ENTITY_FIELD_CODIGO = "codigo";
    private static final String ENTITY_FIELD_NOMBRE = "nombre";

    public RolCriteriaProcessor() {
        super(Rol.class);
    }

    public enum QueryProperty {
        CODIGO, NOMBRE
    }

    @Override
    public void registerProcessors() {
        //@formatter:off
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.NOMBRE)
                .withEntityProperty(ENTITY_FIELD_NOMBRE)
                .build());
        
		registerOrderProcessor(
	            OrderProcessorBuilder.orderProcessor()
	                .withQueryProperty(QueryProperty.CODIGO)
	                .withEntityProperty(ENTITY_FIELD_CODIGO)
	            .build());
    	
    	registerOrderProcessor(
    			OrderProcessorBuilder.orderProcessor()
    			.withQueryProperty(QueryProperty.NOMBRE)
    			.withEntityProperty(ENTITY_FIELD_NOMBRE)
    			.build());
        //@formatter:on
    }

}
