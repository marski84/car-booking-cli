package org.booking.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.booking.domain.car.Car;
import org.booking.domain.user.User;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

@AllArgsConstructor
@Builder
@Getter
public class Booking {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private long id;
    private User user;
    private Car car;
    private Instant startDate;
    private Instant endDate;
    private Instant bookDate;
    private BigDecimal price;
    private BookingStatus bookingStatus;


    Booking(final User user, final Car car, final Instant startDate, final Instant endDate) {
        if (startDate.isAfter(endDate)) {
            throw new DateTimeException("Booking start date should be before booking end date");
        }

        if (startDate.equals(endDate)) {
            throw new DateTimeException("Dates cannot be equal");
        }

        this.user = user;
        this.car = car;
        this.startDate = startDate;
        this.endDate = endDate;

        this.id = ID_GENERATOR.getAndIncrement();
        this.bookDate = Instant.now();

        long seconds = Duration.between(startDate, endDate).getSeconds();
        long days = (seconds + 86399) / 86400;
        this.price = car.getPricePerDay().multiply(BigDecimal.valueOf(days));
        this.bookingStatus = BookingStatus.ACTIVE;

    }

    public static Booking fromParams(String[] params) {
        return Booking.builder()
                .id(Long.parseLong(params[0]))
                .user(User.builder().id(Long.parseLong(params[1])).build())
                .car(Car.builder().id(Integer.parseInt(params[2])).build())
                .startDate(Instant.parse(params[3]))
                .endDate(Instant.parse(params[4]))
                .bookDate(Instant.parse(params[5]))
                .price(new BigDecimal(params[6]))
                .bookingStatus(BookingStatus.valueOf(params[7]))
                .build();
    }
}
