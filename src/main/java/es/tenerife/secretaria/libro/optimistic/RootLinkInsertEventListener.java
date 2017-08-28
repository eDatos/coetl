package es.tenerife.secretaria.libro.optimistic;

import java.util.Map;

import org.hibernate.LockMode;
import org.hibernate.event.spi.PersistEvent;
import org.hibernate.event.spi.PersistEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RootLinkInsertEventListener implements PersistEventListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4237699465034957525L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RootLinkInsertEventListener.class);

	public static final RootLinkInsertEventListener INSTANCE = new RootLinkInsertEventListener();

	@Override
	public void onPersist(PersistEvent event) {
		final Object entity = event.getObject();

		if (entity instanceof RootLink) {
			RootLink<?> rootLink = (RootLink<?>) entity;
			Object root = rootLink.root();
			event.getSession().lock(root, LockMode.OPTIMISTIC_FORCE_INCREMENT);

			if (LOGGER.isDebugEnabled() && root instanceof VersionedEntity) {
				Long version = ((VersionedEntity) root).getOptLock();
				if (version != null && version > 0) {
					LOGGER.debug("Incrementing {} entity version because a {} child entity has been inserted", root,
							entity);
				}
			}
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void onPersist(PersistEvent event, Map createdAlready) {
		onPersist(event);
	}
}