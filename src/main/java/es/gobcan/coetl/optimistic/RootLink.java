package es.gobcan.coetl.optimistic;

import es.gobcan.coetl.domain.VersionedEntity;

@FunctionalInterface
public interface RootLink<T extends VersionedEntity> {

    T root();
}