package org.booking.domain.car;

import org.booking.domain.repository.LineMapper;

public class CarMapper implements LineMapper<Car> {

    @Override
    public String[] parseLine(String line) {
        return line.split(";");
    }

    @Override
    public Car serialize(String[] params) {
        return Car.fromParams(params);
    }
}
