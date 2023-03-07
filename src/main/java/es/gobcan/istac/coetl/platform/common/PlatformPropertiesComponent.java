package es.gobcan.istac.coetl.platform.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.gobcan.istac.coetl.config.ApacheHopProperties;
import es.gobcan.istac.coetl.config.PentahoProperties;
import es.gobcan.istac.coetl.config.common.PlatformProperties;
import es.gobcan.istac.coetl.domain.Etl;

@Component
public class PlatformPropertiesComponent {
    
    @Autowired
    private PentahoProperties pentahoProperties;
    
    @Autowired
    private ApacheHopProperties hopProperties;
    
    public PlatformProperties determinePropertiesClass(Etl etl) {
        switch (etl.getExecutionPlatform()) {
            case APACHE_HOP: return hopProperties;
            case PENTAHO: return pentahoProperties;
            default: {
                throw new RuntimeException("No se pudo determinar la plataforma de ejecución");
            }
        }
    }
    
}
