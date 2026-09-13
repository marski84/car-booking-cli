package org.booking.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserFacadeTest {
    private final Path path = Path.of(getClass().getResource("/users_inline_test_db.txt").toURI());

    private UserFacade userFacade = UserFacade.create(path);

    UserFacadeTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() {
        userFacade = UserFacade.create(path);
    }

    @Test
    void getAllShouldReturnAllUsers() {
        // given, when
        Set<User> users = userFacade.getAll();
        // then
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    void getShouldReturnUserById() {
        // given
        long testId = 1;
        // when
        User user = userFacade.get(testId);
        // then
        assertThat(user.getId()).isEqualTo(testId);
        assertThat(user.getName()).isEqualTo("Anna Kowalska");
    }

    @Test
    void getShouldThrowWhenUserNotFound() {
        // given
        long testId = 999;
        // when, then
        assertThatThrownBy(() -> userFacade.get(testId))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("id 999");
    }
}
