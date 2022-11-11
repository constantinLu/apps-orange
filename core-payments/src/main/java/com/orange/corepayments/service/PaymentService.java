package com.orange.corepayments.service;

import com.orange.corepayments.model.Payment;
import com.orange.corepayments.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.UUID;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;
import static com.orange.corepayments.client.PaymentStatusType.UNPROCESSED;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findPayments(List<UUID> requestIds) {
        return paymentRepository.findByRequestIdIn(requestIds);
    }

    public Payment authorizePayment(Payment incomingPayment) {
        Assert.isTrue(incomingPayment.getPaymentStatus().getType().equals(UNPROCESSED), "Incoming Payment should be UNPROCESSED");

        return paymentRepository.save(incomingPayment.authorizePayment(incomingPayment.getAmount(), incomingPayment.getRequestId()));
    }

    public Payment confirmPayment(Payment incomingPayment) {
        final var originalPayment = paymentRepository.findByRequestId(incomingPayment.getRequestId());
        Assert.isTrue(originalPayment.getPaymentStatus().getType().equals(PENDING_AUTHORIZATION), "Incoming Payment should be AUTHORIZED");
        Assert.isTrue(!originalPayment.getAmount().equals(incomingPayment.getAmount()), "PRE-AUTHORIZED payment should have the full amount");

        return paymentRepository.save(originalPayment.confirmPayment(incomingPayment));
    }
}
