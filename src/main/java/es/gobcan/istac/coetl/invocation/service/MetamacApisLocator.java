package es.gobcan.istac.coetl.invocation.service;

import org.apache.cxf.jaxrs.client.JAXRSClientFactory;
import org.apache.cxf.jaxrs.client.WebClient;
import org.siemac.edatos.core.common.conf.ConfigurationService;
import org.siemac.metamac.statistical_operations.rest.internal.v1_0.service.StatisticalOperationsRestInternalFacadeV10;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetamacApisLocator {

    @Autowired
    private ConfigurationService configurationService;

    private StatisticalOperationsRestInternalFacadeV10 operationsV10 = null;

    public StatisticalOperationsRestInternalFacadeV10 getStatisticalOperationsRestInternalFacadeV10() {
        if (operationsV10 == null) {
            String baseApiUrl = configurationService.retrieveStatisticalOperationsInternalApiUrlBase();
            operationsV10 = JAXRSClientFactory.create(baseApiUrl, StatisticalOperationsRestInternalFacadeV10.class, null, true);
        }

        WebClient.client(operationsV10).reset();
        WebClient.client(operationsV10).accept("application/xml");

        return operationsV10;
    }

}
