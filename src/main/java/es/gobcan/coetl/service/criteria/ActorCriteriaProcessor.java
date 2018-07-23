package es.gobcan.coetl.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.criterion.Criterion;

import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

import es.gobcan.coetl.domain.Actor;
import es.gobcan.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.service.criteria.util.CriteriaUtil;

public class ActorCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_ID = "id";
    private static final String TABLE_FIELD_NOMBRE = "nombre";
    private static final String TABLE_FIELD_APELLIDO1 = "apellido_1";
    private static final String TABLE_FIELD_APELLIDO2 = "apellido_2";

    private static final String ENTITY_FIELD_APELLIDO1 = "apellido1";

    public ActorCriteriaProcessor() {
        super(Actor.class);
    }

    public enum QueryProperty {
        ID, ACTOR, APELLIDO1
    }

    public void registerProcessors() {
        //@formatter:off
        registerRestrictionProcessor(
                RestrictionProcessorBuilder.longRestrictionProcessor()
                .withQueryProperty(QueryProperty.ID)
                .withEntityProperty(TABLE_FIELD_ID)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ACTOR)
                .withCriterionConverter(new SqlCriterionConverter())
                .build());
        registerOrderProcessor(
                OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.APELLIDO1)
                .withEntityProperty(ENTITY_FIELD_APELLIDO1)
                .build());
        //@formatter:on
    }

    public static class SqlCriterionConverter implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if (QueryProperty.ACTOR.name().equalsIgnoreCase(property.getLeftExpression()) && "ILIKE".equalsIgnoreCase(property.getOperationType().name())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_NOMBRE, TABLE_FIELD_APELLIDO1, TABLE_FIELD_APELLIDO2));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Parámetro de búsqueda no soportado: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }
    }
}
