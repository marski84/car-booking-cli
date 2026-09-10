package org.booking.domain.repository;

import java.util.Set;

public interface FileRepositoryInterface<T> {
    Set<T> getAll();

    T get(long id);

    void save(T item);
}
