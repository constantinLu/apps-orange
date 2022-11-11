package com.orange.corepayments.service;

import com.orange.corepayments.client.PaymentDto;
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

    public void confirmPayment(PaymentDto paymentRequest) {
        paymentValidationService.validatePayment(toPayment(paymentRequest));
        paymentService.confirmPayment(paymentRequest);
    }
}
