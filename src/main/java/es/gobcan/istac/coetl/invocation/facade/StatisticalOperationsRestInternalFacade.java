package es.gobcan.istac.coetl.invocation.facade;

import org.siemac.metamac.rest.statistical_operations_internal.v1_0.domain.Operations;

public interface StatisticalOperationsRestInternalFacade {

    public static final String BEAN_ID = "statisticalOperationsInternalFacade";

    public Operations findOperations(int firstResult, int maxResult, String[] operationCodes, String criteria);
}

