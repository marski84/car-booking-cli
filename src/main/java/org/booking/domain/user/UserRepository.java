package org.booking.domain.user;

import lombok.extern.slf4j.Slf4j;
import org.booking.domain.repository.FileRepository;
import org.booking.domain.repository.FileRepositoryInterface;
import org.booking.domain.repository.LineMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class UserRepository extends FileRepository<User> implements FileRepositoryInterface<User> {
    private final Set<User> userList = new HashSet<>();

    public UserRepository(final Path path, LineMapper<User> lineMapper) {
        super(path, lineMapper);
    }

    @Override
    public Set<User> getAll() {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                User user = this.lineMapper.serialize(this.lineMapper.parseLine(line));
                userList.add(user);
            }
        } catch (IOException e) {
            log.error("problem accessing user db file: {}", e.getMessage());
        }
        return Set.copyOf(userList);
    }

    @Override
    public User get(long id) {
        return userList.stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new UserNotFoundException(id);
                });
    }

    @Override
    public void save(User item) {

    }


}
