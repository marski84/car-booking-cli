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
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    private final PrintStream originalOut = System.out;
    @Captor
    ArgumentCaptor<Booking> bookingArgumentCaptor = ArgumentCaptor.forClass(Booking.class);
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
    void setUp() {
        menuService = new MenuService(userFacade, carFacade, bookingFacade);
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    void bookCarShouldHandleBooking() {
        when(userFacade.get(20L)).thenReturn(new User(20L, "Testuser"));
        User user = userFacade.get(20L);

        when(carFacade.get(1L)).thenReturn(car(1L));
        Car car = carFacade.get(1L);

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(20);

        menuService.bookCar(
                String.valueOf(user.getId()),
                String.valueOf(car.getId()),
                startDate,
                endDate);

        verify(bookingFacade).save(bookingArgumentCaptor.capture());
        assertThat(bookingArgumentCaptor.getValue().getCarId()).isEqualTo(car.getId());
        assertThat(bookingArgumentCaptor.getValue().getUserId()).isEqualTo(user.getId());
    }


    @Test
    void bookCarShouldPrintErrorWhenCarNotFound() {
        // given
        when(userFacade.get(20L)).thenReturn(new User(20L, "Testuser"));
        when(carFacade.get(22L)).thenThrow(new RuntimeException("Car with id 22 not found!"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(20);

        // when
        menuService.bookCar("20", "22", startDate, endDate);

        // then
        assertThat(outContent.toString()).contains("Car with id 22 not found!");
        verifyNoInteractions(bookingFacade);
    }

    @Test
    void bookCarShouldPrintErrorWhenUserNotFound() {
        // given
        when(userFacade.get(20L)).thenThrow(new RuntimeException("User with id 20 not found!"));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(20);

        // when
        menuService.bookCar("20", "22", startDate, endDate);

        // then
        assertThat(outContent.toString()).contains("User with id 20 not found!");
        verifyNoInteractions(bookingFacade, carFacade);
    }

    @Test
    void bookCarShouldPrintErrorWhenStartDateAfterEndDate() {
        // given
        when(userFacade.get(20L)).thenReturn(new User(20L, "Testuser"));
        when(carFacade.get(1L)).thenReturn(car(1L));

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().minusDays(
                20);

        // when
        menuService.bookCar("20", "1", startDate, endDate);

        // then
        assertThat(outContent.toString()).contains("Booking start date should be before booking end date");
        verify(bookingFacade, never()).save(any());
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
    void viewAvailableElectricCars_shouldReturn() {
        // given
        Car electricCar = Car.builder()
                .id(4L)
                .electric(true)
                .registrationNumber("abc123")
                .pricePerDay(new BigDecimal("300"))
                .build();
        when(carFacade.getAll()).thenReturn(Set.of(car(1), car(2), car(3), electricCar));
        // when
        menuService.viewAvailableElectricCars();
        // then
        String output = outContent.toString();
        assertThat(output).contains("id=4");
        assertThat(output).doesNotContain("id=1");
        verify(carFacade).getAll();
        verifyNoMoreInteractions(carFacade);
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
        verifyNoInteractions(userFacade);
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

    @Test
    void deleteBookingShouldDelegateToFacade() {
        // when
        menuService.deleteBooking(1);
        // then
        verify(bookingFacade).remove(1);
        verifyNoMoreInteractions(bookingFacade);
    }

    @Test
    void deleteBookingShouldNotCrashWhenFacadeThrows() {
        // given
        doThrow(new RuntimeException("Booking with id 1 not found!")).when(bookingFacade).remove(1);
        // when
        menuService.deleteBooking(1);
        // then
        assertThat(outContent.toString()).contains("Booking with id 1 not found!");
        verifyNoInteractions(carFacade, userFacade);
        verifyNoMoreInteractions(bookingFacade);
    }
}
