package org.booking.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class User {
    private long id;
    private String name;

    public static User fromParams(String[] params) {
        return User.builder()
                .id(Long.parseLong(params[0]))
                .name(params[1]).build();
    }
}
