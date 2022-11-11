package com.orange.corepayments.service;

import com.orange.corepayments.model.Payment;
import com.orange.corepayments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentValidationService {

    private final PaymentRepository paymentRepository;

    public void validatePayment(Payment requestPayment) {
        final var originalPayment = paymentRepository.findByRequestId(requestPayment.getRequestId());
        Assert.isTrue(originalPayment.getPaymentStatus().getType().equals(PENDING_AUTHORIZATION), "Incoming Payment should be AUTHORIZED");
        Assert.isTrue(!originalPayment.getAmount().equals(requestPayment.getAmount()), "PRE-AUTHORIZED payment should have the full amount");

    }
}
