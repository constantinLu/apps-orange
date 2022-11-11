package com.orange.rouber.repository;

import com.orange.rouber.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findPaymentByTrip_assignedTo_id(Long driverId);

}
