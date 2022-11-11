package com.orange.rouber.service;

import com.orange.rouber.model.Driver;
import com.orange.rouber.model.Trip;
import com.orange.rouber.repository.DriverRepository;
import org.springframework.stereotype.Service;

@Service
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverService(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public void registerDriver(Driver driver) {
        driverRepository.save(driver);
    }


    public Driver getDriver(Long driverId) {
        return driverRepository.findById(driverId).orElseThrow();
    }

    protected void processDriverRating(Long driverId) {
        final var driver = getDriver(driverId);

        final var size = driver.getTrips()
                .stream()
                .map(Trip::getRating)
                .filter(rating -> rating != 0)
                .count();

        final var sum = driver.getTrips()
                .stream()
                .map(Trip::getRating)
                .filter(rating -> rating != 0)
                .reduce(0f, Float::sum);

        final var averageRating = sum / size;

        driver.setRating(averageRating);

        driverRepository.save(driver);
    }
}
