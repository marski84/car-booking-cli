package org.booking.domain.user;

import org.booking.domain.repository.LineMapper;

public class UserMapper implements LineMapper<User> {

    @Override
    public String[] parseLine(String line) {
        return line.split(";");
    }

    @Override
    public User serialize(String[] params) {
        return User.fromParams(params);
    }
}
