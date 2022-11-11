package com.orange.rouber.repository;

import com.orange.rouber.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByDriver_Id(Long driverId);

    List<Vehicle> findByStateOrderByCreatedDateDesc(Vehicle.State state);

}
