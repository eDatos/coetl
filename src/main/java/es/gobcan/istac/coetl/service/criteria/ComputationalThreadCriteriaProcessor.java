package es.gobcan.istac.coetl.service.criteria;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.arte.libs.grammar.domain.QueryPropertyRestriction;
import com.arte.libs.grammar.orm.jpa.criteria.AbstractCriteriaProcessor;
import com.arte.libs.grammar.orm.jpa.criteria.CriteriaProcessorContext;
import com.arte.libs.grammar.orm.jpa.criteria.OrderProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.RestrictionProcessorBuilder;
import com.arte.libs.grammar.orm.jpa.criteria.converter.CriterionConverter;

import es.gobcan.istac.coetl.domain.ComputationalThreads;
import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.errors.ErrorConstants;
import es.gobcan.istac.coetl.service.criteria.util.CriteriaUtil;
import es.gobcan.istac.coetl.util.StringUtils;

public class ComputationalThreadCriteriaProcessor extends AbstractCriteriaProcessor {

    private static final String TABLE_FIELD_NAME = "name";

    private static final String ENTITY_FIELD_CODE = "code";
    private static final String ENTITY_FIELD_NAME = "name";

    public ComputationalThreadCriteriaProcessor() {
        super(ComputationalThreads.class);
    }

    public enum QueryProperty {
        CODE, NAME, STATISTICAL_OPERATION, LAST_EXECUTION, LAST_EXECUTION_BY_RESULT, LAST_EXECUTION_CUSTOM
    }

    @Override
    public void registerProcessors() {
        registerOrderProcessor(OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.CODE)
                .withEntityProperty(ENTITY_FIELD_CODE)
                .build());
        registerOrderProcessor(OrderProcessorBuilder.orderProcessor()
                .withQueryProperty(QueryProperty.NAME)
                .withEntityProperty(ENTITY_FIELD_NAME)
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
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.STATISTICAL_OPERATION)
                .withCriterionConverter(new StatisticalOperationCriterionBuilder())
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.LAST_EXECUTION)
                .withCriterionConverter(new LastExecutionCriterionBuilder())
                .build());
        registerProcessorsWithLogicalDeletionPolicy(RestrictionProcessorBuilder.stringRestrictionProcessor()
                .withQueryProperty(QueryProperty.LAST_EXECUTION_BY_RESULT)
                .withCriterionConverter(new LastExecutionByResultCriterionBuilder())
                .build());
        registerProcessorsWithLogicalDeletionPolicy(
                RestrictionProcessorBuilder.restrictionProcessor()
                    .withQueryProperty(QueryProperty.LAST_EXECUTION_CUSTOM)
                    .withCriterionConverter(new LastExecutionCustomCriterionBuilder())
                .build());
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

    private static class StatisticalOperationCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("ILIKE".equals(property.getOperationType().name())) {
                return buildComputationalThreadByExternalItem(property.getRightValue());
            }
            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildComputationalThreadByExternalItem(String value) {
            String sql = String.format("{alias}.external_item_fk IN (SELECT ei.id FROM tb_external_items ei WHERE ei.code ILIKE '%s' OR ei.name ILIKE '%s')",value,value);
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
            String sql = String.format("{alias}.id IN (SELECT e.computational_thread_fk FROM computational_threads_executions e WHERE date(e.start_date) = '%s')",dateValue);
            return Restrictions.sqlRestriction(sql);
        }
    }

    private static class LastExecutionByResultCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("EQ".equals(property.getOperationType().name())) {
                return buildQueryLastExecutionEtlByResult(property.getRightExpression());
            }

            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildQueryLastExecutionEtlByResult(String result) {
            String sql = String.format(" {alias}.id IN (select e.computational_thread_fk FROM computational_threads_executions e where e.\"result\" = %s) ", result);
            return Restrictions.sqlRestriction(sql);
        }

    }

    private static class LastExecutionCustomCriterionBuilder implements CriterionConverter {

        @Override
        public Criterion convertToCriterion(QueryPropertyRestriction property, CriteriaProcessorContext context) {
            if ("IN".equals(property.getOperationType().name())) {
                return buildQueryLastExecutionEtlByResult(property.getRightExpressions());
            }

            throw new CustomParameterizedExceptionBuilder().message(String.format("Search Parameter not supported: '%s'", property))
                    .code(ErrorConstants.QUERY_NO_SOPORTADA, property.getLeftExpression(), property.getOperationType().name()).build();
        }

        private Criterion buildQueryLastExecutionEtlByResult(List<String> result) {
            String dateValue = StringUtils.changeFormatStringDate(result.get(1));
            String sql = String.format(" {alias}.id IN (select e.computational_thread_fk FROM computational_threads_executions e where e.\"result\" = %s and date(e.planning_date) = '%s') "
                    , result.get(0), dateValue);
            return Restrictions.sqlRestriction(sql);
        }
    }

}
