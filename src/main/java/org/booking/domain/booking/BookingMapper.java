package org.booking.domain.booking;

import org.booking.domain.repository.LineMapper;

class BookingMapper implements LineMapper<Booking> {

    @Override
    public String[] parseLine(String line) {
        return line.split(";");
    }

    @Override
    public Booking serialize(String[] params) {
        return Booking.fromParams(params);
    }
}
