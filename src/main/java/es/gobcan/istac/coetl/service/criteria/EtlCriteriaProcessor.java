package es.gobcan.istac.coetl.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

import es.gobcan.istac.coetl.domain.Etl;
import es.gobcan.istac.coetl.domain.Etl.Type;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.criteria.util.CriteriaUtil;
import es.gobcan.istac.coetl.util.StringUtils;

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
        CODE, NAME, TYPE, ORGANIZATION_IN_CHARGE, IS_PLANNED, STATISTICAL_OPERATION, LAST_EXECUTION, NEXT_EXECUTION
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
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.CODE)
                .withEntityProperty(ENTITY_FIELD_CODE)
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.NAME)
                .withCriterionConverter(new NameCriterionBuilder())
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.enumRestrictionProcessor(Type.class)
                .withQueryProperty(QueryProperty.TYPE)
                .withEntityProperty(ENTITY_FIELD_TYPE)
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.ORGANIZATION_IN_CHARGE)
                .withCriterionConverter(new OrganizationInChargeCriterionBuilder())
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.IS_PLANNED)
                .withCriterionConverter(new IsPlannedCriterionBuilder())
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.STATISTICAL_OPERATION)
            .withCriterionConverter(new StatisticalOperationCriterionBuilder())
            .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.NEXT_EXECUTION)
            .withCriterionConverter(new NextExecutionCriterionBuilder())
            .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
            .withQueryProperty(QueryProperty.LAST_EXECUTION)
            .withCriterionConverter(new LastExecutionCriterionBuilder())
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

    private static class IsPlannedCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("EQ".equals(property.getOperationType().name())) {
                boolean value = Boolean.valueOf(property.getRightExpression());
                return value ? buildEtlByIsPlanned() : buildEtlByIsNotPlanned();
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildEtlByIsPlanned() {
            String sql = "{alias}.execution_planning IS NOT NULL";
            return Restrictions.sqlRestriction(sql);
        }

        private Criterion buildEtlByIsNotPlanned() {
            String sql = "{alias}.execution_planning IS NULL";
            return Restrictions.sqlRestriction(sql);
        }
    }

    private static class StatisticalOperationCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("ILIKE".equals(property.getOperationType().name())) {
                return buildEtlByExternalItem(property.getRightValue());
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildEtlByExternalItem(String value) {
            String sql = String.format("{alias}.external_item_fk IN (SELECT ei.id FROM tb_external_items ei WHERE ei.code ILIKE '%s' OR ei.name ILIKE '%s')",value,value);
            return Restrictions.sqlRestriction(sql);
        }

    }

    private static class NextExecutionCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("EQ".equals(property.getOperationType().name())) {
                return buildEtlByNextExecution(property.getRightExpression());
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildEtlByNextExecution(String value) {
            String dateValue = StringUtils.changeFormatStringDate(value);
            String sql = String.format("date({alias}.next_execution) = '%s'", dateValue);
            return Restrictions.sqlRestriction(sql);
        }
    }

    private static class LastExecutionCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("EQ".equals(property.getOperationType().name())) {
                return buildEtlByLastExecution(property.getRightExpression());
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildEtlByLastExecution(String value) {
            String dateValue = StringUtils.changeFormatStringDate(value);
            String sql = String.format("{alias}.id IN (SELECT execution.etl_fk FROM tb_executions execution WHERE date(execution.start_date) = '%s')",dateValue);
            return Restrictions.sqlRestriction(sql);
        }
    }
}
