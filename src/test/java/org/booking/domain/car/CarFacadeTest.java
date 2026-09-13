package org.booking.domain.car;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class CarFacadeTest {
    private final Path path = Path.of(getClass().getResource("/cars_inline_test_db.txt").toURI());

    private CarFacade carFacade = CarFacade.create(path);

    CarFacadeTest() throws URISyntaxException {
    }

    @BeforeEach
    void setUp() {
        carFacade = CarFacade.create(path);
    }

    @Test
    void getAllShouldReturnAllCars() {
        // given, when
        Set<Car> cars = carFacade.getAll();
        // then
        assertThat(cars.size()).isEqualTo(6);
    }

    @Test
    void getShouldReturnCarById() {
        // given
        long testId = 1;
        // when
        Car car = carFacade.get(testId);
        // then
        assertThat(car.getId()).isEqualTo(testId);
        assertThat(car.getRegistrationNumber()).isEqualTo("WA12345");
    }

    @Test
    void getShouldThrowWhenCarNotFound() {
        // given
        long testId = 999;
        // when, then
        assertThatThrownBy(() -> carFacade.get(testId))
                .isInstanceOf(CarNotFoundException.class)
                .hasMessageContaining("id 999");
    }
}
