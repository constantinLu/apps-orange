package com.orange.corepayments.repository;

import com.orange.corepayments.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Payment findByRequestId(UUID requestId);

    List<Payment> findByRequestIdIn(List<UUID> requestIds);

}
