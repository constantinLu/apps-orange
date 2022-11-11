package com.orange.corepayments.repository;

import com.orange.corepayments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByRequestId(String requestId);

    List<Payment> findByRequestIdIn(List<String> requestIds);

}
