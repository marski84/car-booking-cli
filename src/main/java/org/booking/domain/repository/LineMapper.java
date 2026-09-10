package org.booking.domain.repository;

public interface LineMapper<T> {
    String[] parseLine(String line);

    T serialize(String[] params);
}
