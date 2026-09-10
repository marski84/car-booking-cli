package org.booking.domain.car;

import java.math.BigDecimal;

public record CarDto(
        String registrationNumber,
        String Brand,
        BigDecimal pricePerDay,
        boolean electric
) {
}
