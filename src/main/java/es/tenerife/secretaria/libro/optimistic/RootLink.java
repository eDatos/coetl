package es.tenerife.secretaria.libro.optimistic;

public interface RootLink<T extends VersionedEntity> {

	T root();
}