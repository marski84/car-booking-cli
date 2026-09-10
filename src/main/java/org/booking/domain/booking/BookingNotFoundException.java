package org.booking.domain.booking;

class BookingNotFoundException extends RuntimeException {

    public BookingNotFoundException(long bookingId) {
        super("Booking with id " + bookingId + " not found!");
    }
}
