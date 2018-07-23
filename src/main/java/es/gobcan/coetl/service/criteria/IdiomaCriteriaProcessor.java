package es.gobcan.coetl.service.criteria;

import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;

import es.gobcan.coetl.domain.Idioma;

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