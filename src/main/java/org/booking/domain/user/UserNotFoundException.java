package org.booking.domain.user;

class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(final long id) {
        super("User with id " + id + " not found!");
    }
}
