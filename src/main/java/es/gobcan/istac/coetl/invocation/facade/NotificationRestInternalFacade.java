package es.gobcan.istac.coetl.invocation.facade;

import es.gobcan.istac.coetl.domain.Etl;

public interface NotificationRestInternalFacade {

    public static final String BEAN_ID = "notificationRestInternalFacade";

    public void sendExecutionErrorEtlNotice(Etl etl);
}
