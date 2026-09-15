package org.booking.domain.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.booking.domain.car.Car;
import org.booking.domain.user.User;

import java.math.BigDecimal;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Booking {

    private long id;
    private long userId;
    private long carId;
    private Instant startDate;
    private Instant endDate;
    private Instant bookDate;
    private BigDecimal price;
    private BookingStatus bookingStatus;


    public Booking(long id, User user, Car car, Instant startDate, Instant endDate) {
        if (startDate.isAfter(endDate)) {
            throw new DateTimeException("Booking start date should be before booking end date");
        }

        if (startDate.equals(endDate)) {
            throw new DateTimeException("Dates cannot be equal");
        }

        this.userId = user.getId();
        this.carId = car.getId();
        this.startDate = startDate;
        this.endDate = endDate;

        this.id = id;
        this.bookDate = Instant.now();

        long seconds = Duration.between(startDate, endDate).getSeconds();
        long days = (seconds + 86399) / 86400;
        this.price = car.getPricePerDay().multiply(BigDecimal.valueOf(days));
        this.bookingStatus = BookingStatus.ACTIVE;

    }

    public static Booking fromParams(String[] params) {
        return Booking.builder()
                .id(Long.parseLong(params[0]))
                .userId(Long.parseLong(params[1]))
                .carId(Long.parseLong(params[2]))
                .startDate(Instant.parse(params[3]))
                .endDate(Instant.parse(params[4]))
                .bookDate(Instant.parse(params[5]))
                .price(new BigDecimal(params[6]))
                .bookingStatus(BookingStatus.valueOf(params[7]))
                .build();
    }
}
