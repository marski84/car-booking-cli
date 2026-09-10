package org.booking.domain.repository;

import java.nio.file.Path;

public class FileRepository<T> {
    protected final Path path;
    protected final LineMapper<T> lineMapper;

    public FileRepository(final Path path, final LineMapper<T> lineMapper) {
        this.path = path;
        this.lineMapper = lineMapper;
    }


}
