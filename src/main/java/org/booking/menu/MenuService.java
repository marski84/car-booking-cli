package org.booking.menu;

import lombok.extern.slf4j.Slf4j;
import org.booking.domain.booking.Booking;
import org.booking.domain.booking.BookingFacade;
import org.booking.domain.booking.BookingStatus;
import org.booking.domain.car.Car;
import org.booking.domain.car.CarFacade;
import org.booking.domain.user.User;
import org.booking.domain.user.UserFacade;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class MenuService {

    private final UserFacade userFacade;
    private final CarFacade carFacade;
    private final BookingFacade bookingFacade;

    public MenuService() throws URISyntaxException {
        Path userDb = Path.of(getClass().getResource("/users_inline_db.txt").toURI());
        Path carsDb = Path.of(getClass().getResource("/cars_inline_db.txt").toURI());
        Path bookingsDb = Path.of(getClass().getResource("/bookings_inline_db.txt").toURI());
        this.userFacade = UserFacade.create(userDb);
        this.carFacade = CarFacade.create(carsDb);
        this.bookingFacade = BookingFacade.create(bookingsDb);
    }

    MenuService(final UserFacade userFacade, final CarFacade carFacade, final BookingFacade bookingFacade) {
        this.userFacade = userFacade;
        this.carFacade = carFacade;
        this.bookingFacade = bookingFacade;
    }

    void bookCar(String userId, String carId, LocalDate startDate, LocalDate endDate) {
        try {
            User user = this.userFacade.get(Long.parseLong(userId));
            Car car = this.carFacade.get(Long.parseLong(carId));

            Booking booking = new Booking(
                    this.bookingFacade.getMaxId() + 1,
                    user,
                    car,
                    startDate.atStartOfDay(ZoneOffset.UTC).toInstant(),
                    endDate.atStartOfDay(ZoneOffset.UTC).toInstant()
            );

            System.out.println(booking);

            this.bookingFacade.save(booking);


        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
        }
    }

    void handleDeleteBooking() {
    }

    private Set<Long> activeBookedCarIds() {
        return bookingFacade.getAll().stream()
                .filter(b -> b.getBookingStatus() == BookingStatus.ACTIVE)
                .map(Booking::getCarId)
                .collect(Collectors.toSet());
    }

    void viewAllBookedCars() {
        List<Car> bookedCarlist = activeBookedCarIds().stream()
                .map(carFacade::get)
                .sorted(Comparator.comparing(Car::getId))
                .toList();
        bookedCarlist.forEach(c -> System.out.println(c.toString()));
    }

    void viewAllBookings() {
        bookingFacade.getAll().forEach(b -> System.out.println(b.toString()));
    }

    void viewAvailableCars() {
        Set<Long> bookedCarIds = activeBookedCarIds();
        carFacade.getAll().stream()
                .filter(c -> !bookedCarIds.contains(c.getId()))
                .sorted(Comparator.comparing(Car::getId))
                .forEach(System.out::println);
    }

    void viewAvailableElectricCars() {
        Set<Long> bookedCarIds = activeBookedCarIds();
        carFacade.getAll().stream()
                .filter(c -> !bookedCarIds.contains(c.getId()) && c.isElectric())
                .sorted(Comparator.comparing(Car::getId))
                .forEach(System.out::println);
    }

    void viewAllUsers() {
        userFacade.getAll().stream()
                .sorted(Comparator.comparing(User::getId))
                .forEach(System.out::println);
    }
}
