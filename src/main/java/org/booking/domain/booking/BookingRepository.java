package org.booking.domain.booking;

import lombok.extern.slf4j.Slf4j;
import org.booking.domain.repository.FileRepository;
import org.booking.domain.repository.FileRepositoryInterface;
import org.booking.domain.repository.LineMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Slf4j
class BookingRepository extends FileRepository<Booking> implements FileRepositoryInterface<Booking> {

    private final Set<Booking> bookings = new HashSet<>();

    public BookingRepository(final Path path, final LineMapper<Booking> lineMapper) {
        super(path, lineMapper);
    }

    @Override
    public Set<Booking> getAll() {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                Booking booking = this.lineMapper.serialize(this.lineMapper.parseLine(line));
                bookings.add(booking);
            }
        } catch (IOException e) {
            log.error("problem accessing booking db file: {}", e.getMessage());
        }
        return Set.copyOf(bookings);
    }

    @Override
    public Booking get(long id) {
        return bookings.stream()
                .filter(booking -> booking.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Booking with id {} not found", id);
                    return new BookingNotFoundException(id);
                });
    }

    @Override
    public void save(Booking item) {

    }
}
