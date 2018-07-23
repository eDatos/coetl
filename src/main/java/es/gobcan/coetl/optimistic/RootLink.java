package es.gobcan.coetl.optimistic;

@FunctionalInterface
public interface RootLink<T extends VersionedEntity> {

    T root();
}