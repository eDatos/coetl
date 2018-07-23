package es.gobcan.coetl.optimistic;

import org.hibernate.boot.Metadata;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;

public class RootLinkEventListenerIntegrator implements Integrator {

    public static final RootLinkEventListenerIntegrator INSTANCE = new RootLinkEventListenerIntegrator();

    @Override
    public void integrate(Metadata metadata, SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        // Integrate allows us to hook into the building process

        final EventListenerRegistry eventListenerRegistry = serviceRegistry.getService(EventListenerRegistry.class);

        eventListenerRegistry.appendListeners(EventType.PERSIST, RootLinkInsertEventListener.INSTANCE);
        eventListenerRegistry.appendListeners(EventType.FLUSH_ENTITY, RootLinkUpdateAndDeleteEventListener.INSTANCE);
    }

    @Override
    public void disintegrate(SessionFactoryImplementor sessionFactory, SessionFactoryServiceRegistry serviceRegistry) {
        // Desintegrate allows us to hook into a SessionFactory shutting down
    }
}