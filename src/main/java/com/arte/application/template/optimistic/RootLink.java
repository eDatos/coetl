package com.arte.application.template.optimistic;

@FunctionalInterface
public interface RootLink<T extends VersionedEntity> {

    T root();
}