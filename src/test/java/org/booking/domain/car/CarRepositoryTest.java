package org.booking.domain.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarRepositoryTest {
    Path path = Path.of(getClass().getResource("/cars_inline_test_db.txt").toURI());
    CarMapper carMapper = new CarMapper();

    private CarRepository carRepository = new CarRepository(path, carMapper);

    CarRepositoryTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() {
        carRepository = new CarRepository(path, carMapper);
    }

    @Test
    void getAllShouldReturn() {
        // given, when
        Set<Car> allCars = carRepository.getAll();
        // then
        assertThat(allCars.size()).isEqualTo(6);
    }

    @Test
    void getShouldReturn() {
        // given
        carRepository.getAll();
        int testId = 1;
        String testBrand = "Toyota";
        // when
        Car car = carRepository.get(testId);
        // then
        assertThat(car.getId() == testId);
        assertThat(car.getRegistrationNumber().equals(testBrand));
    }

    @Test
    void getShouldThrowWhenCarListNotInitiated() {
        // given
        int testId = 1;
        String testBrand = "Toyota";
        // when, then
        assertThatThrownBy(() -> carRepository.get(testId))
                .hasMessageContaining("id 1");
    }
}