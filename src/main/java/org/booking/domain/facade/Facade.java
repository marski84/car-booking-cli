package org.booking.domain.facade;

import java.util.Set;

public interface Facade<T> {
    Set<T> getAll();

    T get(long id);

    void save(T item);
}
