package org.booking.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class User {
    private long id;
    private String name;

    public static User fromParams(String[] params) {
        return User.builder()
                .id(Long.parseLong(params[0]))
                .name(params[1]).build();
    }
}
