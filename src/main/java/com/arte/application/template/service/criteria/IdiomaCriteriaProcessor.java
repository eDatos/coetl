package com.arte.application.template.service.criteria;

import com.arte.application.template.domain.Idioma;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

public class IdiomaCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String ENTITY_FIELD_NOMBRE = "nombre";
    
    public IdiomaCriteriaProcessor() {
        super(Idioma.class);
    }

    public enum QueryProperty {
        NOMBRE
    }

    @Override
    public void registerProcessors() {
      //@formatter:off
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.NOMBRE)
                .withEntityProperty(ENTITY_FIELD_NOMBRE)
                .build());
        //@formatter:on   
    }
}