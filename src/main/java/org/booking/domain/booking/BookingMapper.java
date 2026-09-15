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

    @Override
    public String toLine(final Booking item) {
        return String.join(";",
                String.valueOf(item.getId()),
                String.valueOf(item.getUserId()),
                String.valueOf(item.getCarId()),
                item.getStartDate().toString(),
                item.getEndDate().toString(),
                item.getBookDate().toString(),
                item.getPrice().toString(),
                item.getBookingStatus().name()
        );
    }
}
