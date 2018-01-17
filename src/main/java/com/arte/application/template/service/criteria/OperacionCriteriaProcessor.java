package com.arte.application.template.service.criteria;

import com.arte.application.template.domain.Operacion;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

public class OperacionCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String ENTITY_FIELD_ACCION = "accion";
    private static final String ENTITY_FIELD_SUJETO = "sujeto";

    public OperacionCriteriaProcessor() {
        super(Operacion.class);
    }

    public enum QueryProperty {
        ACCION, SUJETO, ID
    }

    @Override
    public void registerProcessors() {
        //@formatter:off
    	registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ACCION)
                .withEntityProperty(ENTITY_FIELD_ACCION)
                .build());
		
		registerOrderProcessor(
	            OrderProcessorBuilder.orderProcessor()
	                .withQueryProperty(QueryProperty.ACCION)
	                .withEntityProperty(ENTITY_FIELD_ACCION)
	            .build());
    	
    	registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
    			.withQueryProperty(QueryProperty.SUJETO)
    			.withEntityProperty(ENTITY_FIELD_SUJETO)
    			.sortable()
    			.build());

    	registerOrderProcessor(
    			OrderProcessorBuilder.orderProcessor()
    			.withQueryProperty(QueryProperty.SUJETO)
    			.withEntityProperty(ENTITY_FIELD_SUJETO)
    			.build());
    	
    	registerRestrictionProcessor(RestrictionProcessorBuilder.longRestrictionProcessor()
    			.withQueryProperty(QueryProperty.ID)
    			.withEntityProperty(P_ID)
    			.sortable().build());
        //@formatter:on
    }

}
