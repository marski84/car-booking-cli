package org.booking.domain.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
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
    void getShouldThrowWhenBookingNotFound() {
        // given
        long testId = 999;
        // when, then
        assertThatThrownBy(() -> bookingRepository.get(testId))
                .hasMessageContaining("id 999");
    }

    @Test
    void removeShouldCancelBookingAndKeepFileParsable(@TempDir Path tempDir) throws IOException {
        // given
        Path tempFile = tempDir.resolve("bookings_inline_test_db.txt");
        Files.copy(path, tempFile, StandardCopyOption.REPLACE_EXISTING);
        BookingRepository repository = new BookingRepository(tempFile, bookingMapper);
        repository.getAll();

        // when
        repository.remove(1);

        // then
        Booking cancelled = repository.get(1);
        assertThat(cancelled.getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(cancelled.getPrice()).isEqualByComparingTo(java.math.BigDecimal.ZERO);

        // and: the file must still be readable by a brand new repository instance (proves lines weren't merged)
        BookingRepository reloaded = new BookingRepository(tempFile, bookingMapper);
        Set<Booking> allBookings = reloaded.getAll();
        assertThat(allBookings).hasSize(3);
        assertThat(reloaded.get(1).getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
    }

    @Test
    void removeShouldThrowWhenBookingNotFound(@TempDir Path tempDir) throws IOException {
        // given
        Path tempFile = tempDir.resolve("bookings_inline_test_db.txt");
        Files.copy(path, tempFile, StandardCopyOption.REPLACE_EXISTING);
        BookingRepository repository = new BookingRepository(tempFile, bookingMapper);

        // when, then
        assertThatThrownBy(() -> repository.remove(999))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("id 999");
    }
}
