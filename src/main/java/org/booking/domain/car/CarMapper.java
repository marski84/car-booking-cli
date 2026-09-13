package org.booking.domain.car;

import org.booking.domain.repository.LineMapper;

class CarMapper implements LineMapper<Car> {

    @Override
    public String[] parseLine(String line) {
        return line.split(";");
    }

    @Override
    public Car serialize(String[] params) {
        return Car.fromParams(params);
    }

    @Override
    public String toLine(final Car item) {
        return "";
    }
}
