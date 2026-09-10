package org.booking.domain.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BookingRepositoryTest {
    Path path = Path.of(getClass().getResource("/bookings_inline_test_db.txt").toURI());
    BookingMapper bookingMapper = new BookingMapper();

    private BookingRepository bookingRepository = new BookingRepository(path, bookingMapper);

    BookingRepositoryTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() {
        bookingRepository = new BookingRepository(path, bookingMapper);
    }

    @Test
    void getAllShouldReturn() {
        // given, when
        Set<Booking> allBookings = bookingRepository.getAll();
        // then
        assertThat(allBookings.size()).isEqualTo(3);
    }

    @Test
    void getShouldReturn() {
        // given
        bookingRepository.getAll();
        long testId = 1;
        // when
        Booking booking = bookingRepository.get(testId);
        // then
        assertThat(booking.getId()).isEqualTo(testId);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.ACTIVE);
    }

    @Test
    void getShouldThrowWhenBookingListNotInitiated() {
        // given
        long testId = 1;
        // when, then
        assertThatThrownBy(() -> bookingRepository.get(testId))
                .hasMessageContaining("id 1");
    }
}
