package es.gobcan.istac.coetl.invocation.facade;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.siemac.edatos.core.common.exception.CommonServiceExceptionParameters;
import org.siemac.metamac.rest.common.v1_0.domain.ComparisonOperator;
import org.siemac.metamac.rest.common.v1_0.domain.LogicalOperator;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.OperationCriteriaPropertyRestriction;
import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.errors.CustomParameterizedExceptionBuilder;
import es.gobcan.istac.coetl.invocation.service.MetamacApisLocator;

@Component(StatisticalOperationsRestInternalFacade.BEAN_ID)
public class StatisticalOperationsRestInternalFacadeImpl implements StatisticalOperationsRestInternalFacade {

    @Autowired
    private MetamacApisLocator restApiLocator;

    @Override
    public Operations findOperations(int firstResult, int maxResult, String[] operationCodes, String criteria){
        try {
            String limit = String.valueOf(maxResult);
            String offset = String.valueOf(firstResult);
            String query = buildQuery(operationCodes, criteria);

            Operations findOperationsResult = restApiLocator.getStatisticalOperationsRestInternalFacadeV10().findOperations(query, "ASC", limit, offset);

            return findOperationsResult;
        } catch (Exception e) {
            throw new CustomParameterizedExceptionBuilder().message(String.format("Operation Internal API doesn't connect. Trace: [ "+e+" ]"))
                    .code(CommonServiceExceptionParameters.API_STATISTICAL_OPERATIONS_INTERNAL).build();
        }
    }

    private String buildQuery(String[] operationCodes, String criteria) {
        StringBuilder queryBuilder = new StringBuilder();
        if (StringUtils.isNotBlank(criteria)) {
            queryBuilder.append("(").append(OperationCriteriaPropertyRestriction.TITLE).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"");
            queryBuilder.append(" ").append(LogicalOperator.OR.name()).append(" ");
            queryBuilder.append(OperationCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.ILIKE.name()).append(" \"").append(criteria).append("\"").append(")");
        }
        if (ArrayUtils.isNotEmpty(operationCodes)) {
            if (StringUtils.isNotBlank(queryBuilder.toString())) {
                queryBuilder.append(" ").append(LogicalOperator.AND).append(" ");
            }
            queryBuilder.append("(");
            for (int i = 0; i < operationCodes.length; i++) {
                if (i != 0) {
                    queryBuilder.append(" ").append(LogicalOperator.OR).append(" ");
                }
                queryBuilder.append(OperationCriteriaPropertyRestriction.ID).append(" ").append(ComparisonOperator.EQ.name()).append(" \"").append(operationCodes[i]).append("\"");
            }
            queryBuilder.append(")");
        }
        return queryBuilder.toString();
    }
}
