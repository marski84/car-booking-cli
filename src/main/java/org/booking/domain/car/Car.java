package org.booking.domain.car;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Car {
    private final long id;
    private final String registrationNumber;
    private final Brand brand;
    private final boolean electric;
    private BigDecimal pricePerDay;

    public static Car fromParams(String[] params) {
        return Car.builder()
                .id(Long.parseLong(params[0]))
                .registrationNumber(params[1])
                .pricePerDay(new BigDecimal(params[2]))
                .brand(Brand.valueOf(params[3]))
                .electric(Boolean.parseBoolean(params[4]))
                .build();
    }

    public static Car fromDto(CarDto dto, long id) {
        return Car.builder()
                .id(id)
                .registrationNumber(dto.registrationNumber())
                .pricePerDay(dto.pricePerDay())
                .brand(Brand.valueOf(dto.Brand()))
                .electric(dto.electric())
                .build();
    }
}
