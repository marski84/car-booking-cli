package org.booking.domain.booking;

import lombok.AllArgsConstructor;
import org.booking.domain.facade.Facade;

import java.nio.file.Path;
import java.util.Set;

@AllArgsConstructor
public class BookingFacade implements Facade<Booking> {
    private final BookingRepository bookingRepository;

    public static BookingFacade create(Path dbPath) {
        return new BookingFacade(new BookingRepository(dbPath, new BookingMapper()));
    }

    @Override
    public Set<Booking> getAll() {
        return bookingRepository.getAll();
    }

    @Override
    public Booking get(final long id) {
        return bookingRepository.get(id);
    }

    @Override
    public void save(final Booking item) {
        this.bookingRepository.save(item);
    }

    public long getMaxId() {
        return bookingRepository.getMaxId();
    }
}
