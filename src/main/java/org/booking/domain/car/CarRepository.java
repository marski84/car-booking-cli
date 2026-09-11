package org.booking.domain.car;

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
class CarRepository extends FileRepository<Car> implements FileRepositoryInterface<Car> {

    private final Set<Car> carList = new HashSet<>();

    public CarRepository(Path path, LineMapper<Car> lineMapper) {
        super(path, lineMapper);
    }

    @Override
    public Set<Car> getAll() {
        try (BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            String line;

            while ((line = reader.readLine()) != null) {
                Car car = lineMapper.serialize(this.lineMapper.parseLine(line));
                carList.add(car);
            }

        } catch (IOException e) {
            log.error("problem accessing car db file: {}", e.getMessage());
        }
        carList.stream().forEach(c -> log.info(String.valueOf(c)));
        return Set.copyOf(carList);
    }

    @Override
    public Car get(long id) {
        return carList.stream()
                .filter(car -> car.getId() == id)
                .findFirst()
                .orElseThrow(() -> {
                    log.error("Car with id {} not found", id);
                    return new CarNotFoundException(id);
                });
    }

    @Override
    public void save(Car item) {

    }


}
