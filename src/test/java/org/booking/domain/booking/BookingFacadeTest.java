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

class BookingFacadeTest {
    private final Path path = Path.of(getClass().getResource("/bookings_inline_test_db.txt").toURI());

    private BookingFacade bookingFacade = BookingFacade.create(path);

    BookingFacadeTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() {
        bookingFacade = BookingFacade.create(path);
    }

    @Test
    void getAllShouldReturnAllBookings() {
        // given, when
        Set<Booking> bookings = bookingFacade.getAll();
        // then
        assertThat(bookings.size()).isEqualTo(3);
    }

    @Test
    void getShouldReturnBookingById() {
        // given
        long testId = 1;
        // when
        Booking booking = bookingFacade.get(testId);
        // then
        assertThat(booking.getId()).isEqualTo(testId);
        assertThat(booking.getBookingStatus()).isEqualTo(BookingStatus.ACTIVE);
    }

    @Test
    void getShouldThrowWhenBookingNotFound() {
        // given
        long testId = 999;
        // when, then
        assertThatThrownBy(() -> bookingFacade.get(testId))
                .isInstanceOf(BookingNotFoundException.class)
                .hasMessageContaining("id 999");
    }

    @Test
    void removeShouldDelegateToRepositoryAndCancelBooking(@TempDir Path tempDir) throws IOException {
        // given
        Path tempFile = tempDir.resolve("bookings_inline_test_db.txt");
        Files.copy(path, tempFile, StandardCopyOption.REPLACE_EXISTING);
        BookingFacade facade = BookingFacade.create(tempFile);
        facade.getAll();

        // when
        facade.remove(1);

        // then
        assertThat(facade.get(1).getBookingStatus()).isEqualTo(BookingStatus.CANCELLED);
        assertThat(facade.getAll()).hasSize(3);
    }
}
