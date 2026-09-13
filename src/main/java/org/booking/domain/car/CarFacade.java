package org.booking.domain.car;

import lombok.AllArgsConstructor;
import org.booking.domain.facade.Facade;

import java.nio.file.Path;
import java.util.Set;

@AllArgsConstructor
public class CarFacade implements Facade<Car> {
    private final CarRepository carRepository;

    public static CarFacade create(Path dbPath) {
        return new CarFacade(new CarRepository(dbPath, new CarMapper()));
    }

    @Override
    public Set<Car> getAll() {
        return carRepository.getAll();
    }

    @Override
    public Car get(long id) {
        return carRepository.get(id);
    }

    @Override
    public void save(Car item) {

    }
}
