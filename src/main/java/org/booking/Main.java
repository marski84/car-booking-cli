package org.booking;

import org.booking.domain.car.CarMapper;
import org.booking.domain.car.CarRepository;
import org.booking.domain.user.UserMapper;
import org.booking.domain.user.UserRepository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {

        final Path path = Path.of("src/main/resources/cars_inline_db.txt");
        URL usersDbUrl = Main.class.getResource("/users_inline_db.txt");
        final Path userDb = Path.of(usersDbUrl.toURI());

        CarMapper carMapper = new CarMapper();
        UserMapper userMapper = new UserMapper();

        CarRepository carRepository = new CarRepository(path, carMapper);
        UserRepository userRepository = new UserRepository(userDb, userMapper);


//        carRepository.getAll();
        userRepository.getAll().stream().forEach(System.out::println);

    }
}