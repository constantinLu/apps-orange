package com.orange.rouber.service;

import com.orange.rouber.model.Vehicle;
import com.orange.rouber.repository.DriverRepository;
import com.orange.rouber.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.orange.rouber.model.Vehicle.State.ACTIVE;
import static com.orange.rouber.model.Vehicle.State.INACTIVE;

@RequiredArgsConstructor
@Slf4j
@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;

    private final DriverRepository driverRepository;

    public void registerVehicle(Vehicle vehicle, Long driverId) {
        final var driver = driverRepository.findById(driverId).orElseThrow();
        vehicle.setDriver(driver);
        vehicle.setState(ACTIVE);

        archiveOldVehicles(driver.getId());
        vehicleRepository.save(vehicle);

    }

    public List<Vehicle> getVehicleHistory() {
        return vehicleRepository.findByStateOrderByCreatedDateDesc(INACTIVE);
    }

    private void archiveOldVehicles(Long driverId) {
        final var oldVehicles = vehicleRepository.findByDriver_IdAndState(driverId, ACTIVE);
        oldVehicles.forEach(vehicle -> {
            vehicle.setState(INACTIVE);
            vehicleRepository.save(vehicle);
        });
    }
}
