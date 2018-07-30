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

import es.gobcan.coetl.domain.Etl;
import es.gobcan.coetl.domain.Etl.Type;
import es.gobcan.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.coetl.errors.ErrorConstants;
import es.gobcan.coetl.service.criteria.util.CriteriaUtil;

public class EtlCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_NAME = "name";
    private static final String TABLE_FIELD_ORGANIZATION_IN_CHARGE = "organization_in_charge";

    private static final String ENTITY_FIELD_CODE = "code";
    private static final String ENTITY_FIELD_NAME = "name";
    private static final String ENTITY_FIELD_TYPE = "type";
    private static final String ENTITY_FIELD_ORGANIZATION_IN_CHARGE = "organizationInCharge";

    public EtlCriteriaProcessor() {
        super(Etl.class);
    }

    public enum QueryProperty {
        CODE, NAME, TYPE, ORGANIZATION_IN_CHARGE
    }

    @Override
    public void registerProcessors() {
        //@formatter:off
        // Orders
        registerOrderProcessor(OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.CODE)
                .withEntityProperty(ENTITY_FIELD_CODE)
                .build());
        registerOrderProcessor(OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.NAME)
                .withEntityProperty(ENTITY_FIELD_NAME)
                .build());
        registerOrderProcessor(OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.TYPE)
                .withEntityProperty(ENTITY_FIELD_TYPE)
                .build());
        registerOrderProcessor(OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.ORGANIZATION_IN_CHARGE)
                .withEntityProperty(ENTITY_FIELD_ORGANIZATION_IN_CHARGE)
                .build());
        
        // Restrictions
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.CODE)
                .withEntityProperty(ENTITY_FIELD_CODE)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.NAME)
                .withCriterionConverter(new NameCriterionBuilder())
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.enumRestrictionProcessor(Type.class)
                .withQueryProperty(QueryProperty.TYPE)
                .withEntityProperty(ENTITY_FIELD_TYPE)
                .build());
        registerRestrictionProcessor(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ORGANIZATION_IN_CHARGE)
                .withCriterionConverter(new OrganizationInChargeCriterionBuilder())
                .build());
        //@formatter:on
    }

    private static class NameCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("ILIKE".equals(property.getOperationType().name())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_NAME));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }
    }

    private static class OrganizationInChargeCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("ILIKE".equals(property.getOperationType().name())) {
                ArrayList<String> fields = new ArrayList<>(Arrays.asList(TABLE_FIELD_ORGANIZATION_IN_CHARGE));
                return CriteriaUtil.buildAccentAndCaseInsensitiveCriterion(property, fields);
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }
    }
}
