package com.orange.corepayments.service;

import com.orange.corepayments.client.CorePaymentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.orange.corepayments.converter.Converter.toPayment;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentConfirmationOrchestrator {

    private final PaymentService paymentService;
    private final PaymentValidationService paymentValidationService;

    public void confirmPayment(CorePaymentDto paymentRequest) {
        final var payment = toPayment(paymentRequest);
        paymentValidationService.validatePayment(payment);
        paymentService.confirmPayment(payment);
    }
}
