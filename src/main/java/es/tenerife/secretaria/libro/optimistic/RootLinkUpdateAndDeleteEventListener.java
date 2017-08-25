package es.tenerife.secretaria.libro.optimistic;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.engine.spi.EntityEntry;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.Status;
import org.hibernate.event.spi.FlushEntityEvent;
import org.hibernate.event.spi.FlushEntityEventListener;
import org.hibernate.persister.entity.EntityPersister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootLinkUpdateAndDeleteEventListener implements FlushEntityEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1450093309749917447L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RootLinkUpdateAndDeleteEventListener.class);

	public static final RootLinkUpdateAndDeleteEventListener INSTANCE = new RootLinkUpdateAndDeleteEventListener();

	@Override
	public void onFlushEntity(FlushEntityEvent event) throws HibernateException {
		final EntityEntry entry = event.getEntityEntry();
		final Object entity = event.getEntity();
		final boolean mightBeDirty = entry.requiresDirtyCheck(entity);

		if (mightBeDirty && entity instanceof RootLink) {
			RootLink<?> rootLink = (RootLink<?>) entity;
			if (updated(event)) {
				Object root = rootLink.root();
				LOGGER.debug("Incrementing {} entity version because a {} child entity has been updated", root, entity);
				incrementRootVersion(event, root);
			} else if (deleted(event)) {
				Object root = rootLink.root();
				LOGGER.debug("Incrementing {} entity version because a {} child entity has been deleted", root, entity);
				incrementRootVersion(event, root);
			}
		}
	}

	private void incrementRootVersion(FlushEntityEvent event, Object root) {
		event.getSession().lock(root, LockMode.OPTIMISTIC_FORCE_INCREMENT);
	}

	private boolean deleted(FlushEntityEvent event) {
		return event.getEntityEntry().getStatus() == Status.DELETED;
	}

	private boolean updated(FlushEntityEvent event) {
		final EntityEntry entry = event.getEntityEntry();
		final Object entity = event.getEntity();

		int[] dirtyProperties;
		EntityPersister persister = entry.getPersister();
		final Object[] values = event.getPropertyValues();
		SessionImplementor session = event.getSession();

		if (event.hasDatabaseSnapshot()) {
			dirtyProperties = persister.findModified(event.getDatabaseSnapshot(), values, entity, session);
		} else {
			dirtyProperties = persister.findDirty(values, entry.getLoadedState(), entity, session);
		}

		return dirtyProperties != null;
	}
}