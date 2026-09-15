package org.booking.domain.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserRepositoryTest {
    Path path = Path.of(getClass().getResource("/users_inline_test_db.txt").toURI());
    UserMapper userMapper = new UserMapper();

    private UserRepository userRepository = new UserRepository(path, userMapper);

    UserRepositoryTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository(path, userMapper);
    }

    @Test
    void getAllShouldReturn() {
        // given, when
        Set<User> allUsers = userRepository.getAll();
        // then
        assertThat(allUsers.size()).isEqualTo(4);
    }

    @Test
    void getShouldReturn() {
        // given
        userRepository.getAll();
        long testId = 1;
        String testName = "Anna Kowalska";
        // when
        User user = userRepository.get(testId);
        // then
        assertThat(user.getId()).isEqualTo(testId);
        assertThat(user.getName()).isEqualTo(testName);
    }

    @Test
    void getShouldThrowWhenUserNotFound() {
        // given
        long testId = 999;
        // when, then
        assertThatThrownBy(() -> userRepository.get(testId))
                .hasMessageContaining("id 999");
    }
}
