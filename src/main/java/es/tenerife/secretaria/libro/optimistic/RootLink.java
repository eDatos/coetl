package es.tenerife.secretaria.libro.optimistic;

@FunctionalInterface
public interface RootLink<T extends VersionedEntity> {

	T root();
}