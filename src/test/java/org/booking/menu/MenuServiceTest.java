package org.booking.menu;

import org.booking.domain.booking.Booking;
import org.booking.domain.booking.BookingFacade;
import org.booking.domain.booking.BookingStatus;
import org.booking.domain.car.Car;
import org.booking.domain.car.CarFacade;
import org.booking.domain.user.User;
import org.booking.domain.user.UserFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private final PrintStream originalOut = System.out;
    @Mock
    private UserFacade userFacade;
    @Mock
    private CarFacade carFacade;
    @Mock
    private BookingFacade bookingFacade;
    private MenuService menuService;
    private ByteArrayOutputStream outContent;

    private static Car car(long id) {
        return Car.builder()
                .id(id)
                .registrationNumber("REG" + id)
                .electric(false)
                .pricePerDay(BigDecimal.TEN)
                .build();
    }

    private static Booking booking(long id, long carId, BookingStatus status) {
        return Booking.builder()
                .id(id)
                .userId(1L)
                .carId(carId)
                .startDate(Instant.parse("2026-01-01T10:00:00Z"))
                .endDate(Instant.parse("2026-01-03T10:00:00Z"))
                .bookDate(Instant.parse("2025-12-01T09:00:00Z"))
                .price(BigDecimal.valueOf(200))
                .bookingStatus(status)
                .build();
    }

    @BeforeEach
    void setUp() throws URISyntaxException {
        menuService = new MenuService(userFacade, carFacade, bookingFacade);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void viewAllBookedCarsShouldPrintOnlyActiveBookedCars() {
        // given
        when(bookingFacade.getAll()).thenReturn(Set.of(
                booking(1, 2, BookingStatus.ACTIVE),
                booking(2, 3, BookingStatus.CANCELLED)
        ));
        when(carFacade.get(2)).thenReturn(car(2));
        // when
        menuService.viewAllBookedCars();
        // then
        String output = outContent.toString();
        assertThat(output).contains("id=2");
        assertThat(output).doesNotContain("id=3");
    }

    @Test
    void viewAvailableCarsShouldExcludeActiveBookedCars() {
        // given
        when(carFacade.getAll()).thenReturn(Set.of(car(1), car(2), car(3)));
        when(bookingFacade.getAll()).thenReturn(Set.of(
                booking(1, 2, BookingStatus.ACTIVE)
        ));
        // when
        menuService.viewAvailableCars();
        // then
        String output = outContent.toString();
        assertThat(output).contains("id=1");
        assertThat(output).contains("id=3");
        assertThat(output).doesNotContain("id=2");
    }

    @Test
    void viewAllUsersShouldPrintAllUsers() {
        // given
        when(userFacade.getAll()).thenReturn(Set.of(
                User.builder().id(1).name("Anna Kowalska").build(),
                User.builder().id(2).name("John Smith").build()
        ));
        // when
        menuService.viewAllUsers();
        // then
        String output = outContent.toString();
        assertThat(output).contains("Anna Kowalska");
        assertThat(output).contains("John Smith");
        verifyNoInteractions(carFacade, bookingFacade);
    }

    @Test
    void viewAllBookingsShouldPrintEveryBookingRegardlessOfStatus() {
        // given
        when(bookingFacade.getAll()).thenReturn(Set.of(
                booking(1, 2, BookingStatus.ACTIVE),
                booking(2, 3, BookingStatus.CANCELLED)
        ));
        // when
        menuService.viewAllBookings();
        // then
        String output = outContent.toString();
        assertThat(output).contains("id=1");
        assertThat(output).contains("id=2");
        verify(bookingFacade).getAll();
    }
}
