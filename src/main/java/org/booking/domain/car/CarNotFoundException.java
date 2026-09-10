package org.booking.domain.car;

class CarNotFoundException extends RuntimeException {

    public CarNotFoundException(long carId) {
        super("Car with id " + carId + " not found!");
    }
}
