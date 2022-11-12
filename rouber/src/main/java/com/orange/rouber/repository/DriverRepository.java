package com.orange.rouber.repository;

import com.orange.rouber.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {


    Driver findByEmail(String email);

}
