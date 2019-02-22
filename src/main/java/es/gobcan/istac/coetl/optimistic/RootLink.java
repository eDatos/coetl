package es.gobcan.istac.coetl.optimistic;

import es.gobcan.istac.coetl.domain.VersionedEntity;

@FunctionalInterface
public interface RootLink<T extends VersionedEntity> {

    T root();
}