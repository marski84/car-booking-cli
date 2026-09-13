package org.booking.domain.user;

import lombok.AllArgsConstructor;
import org.booking.domain.facade.Facade;

import java.nio.file.Path;
import java.util.Set;

@AllArgsConstructor
public class UserFacade implements Facade<User> {

    private final UserRepository userRepository;

    public static UserFacade create(Path dbPath) {
        return new UserFacade(
                new UserRepository(
                        dbPath,
                        new UserMapper()
                ));
    }

    @Override
    public Set<User> getAll() {
        return userRepository.getAll();
    }

    @Override
    public User get(long id) {
        return userRepository.get(id);
    }

    @Override
    public void save(User item) {

    }
}
