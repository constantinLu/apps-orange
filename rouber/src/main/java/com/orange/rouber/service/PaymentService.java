package com.orange.rouber.service;

import com.orange.rouber.client.corepayments.CorePaymentDto;
import com.orange.rouber.model.Payment;
import com.orange.rouber.model.Trip;
import com.orange.rouber.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.orange.rouber.client.corepayments.PaymentStatusType.PENDING_AUTHORIZATION;
import static com.orange.rouber.client.corepayments.PaymentStatusType.UNPROCESSED;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CorePaymentService corePaymentService;

    public PaymentService(PaymentRepository paymentRepository, CorePaymentService corePaymentService) {
        this.paymentRepository = paymentRepository;
        this.corePaymentService = corePaymentService;
    }

    public Payment createPayment(Trip trip) {
        final var unProcessedPayment = CorePaymentDto.builder()
                .amount(processAuthorizationAmount(trip.getPrice()))
                .requestId(UUID.randomUUID())
                .paymentStatus(UNPROCESSED)
                .reason(Optional.of("Start of trip authorization"))
                .build();

        final var processedPayment = corePaymentService.authorizePayment(unProcessedPayment);

        final var payment = Payment.builder()
                .paidPrice(processedPayment.getBody().getAmount())
                .trip(trip)
                .startInitiation(processedPayment.getBody().getCreatedDate())
                .requestId(processedPayment.getBody().getRequestId())
                .build();
        return paymentRepository.save(payment);
    }

    public void confirmPayment(Long paymentId) {
        final var originalPayment = paymentRepository.findById(paymentId).orElseThrow();
        Assert.isTrue(originalPayment.getStartInitiation().isPresent(), "Payment must be AUTHORIZED");
        Assert.isTrue(originalPayment.getEndConfirmation().isEmpty(), "Payment should not be CONFIRMED YET");
        Assert.isTrue(originalPayment.getPaidPrice().longValue() < originalPayment.getTrip().getPrice().longValue(), "Pre authorized amount should be smaller");

        final var authorizedPaymentRequest = CorePaymentDto.builder()
                .amount(originalPayment.getTrip().getPrice())
                .requestId(originalPayment.getRequestId())
                .paymentStatus(PENDING_AUTHORIZATION)
                .reason(Optional.of("Trip ended. Confirmation of payment required"))
                .build();

        final var confirmedPaymentResponse = corePaymentService.confirmPayment(authorizedPaymentRequest);
        final var confirmedPayment = Objects.requireNonNull(confirmedPaymentResponse.getBody());

        Assert.isTrue(originalPayment.getTrip().getPrice().equals(confirmedPayment.getAmount()), "Amount payed and amount processed must be equal");
        Assert.isTrue(originalPayment.getRequestId().equals(confirmedPayment.getRequestId()), "RequestId must be the same");

        paymentRepository.save(originalPayment.confirmPayment(confirmedPayment.getAmount(), confirmedPayment.getUpdatedDate()));
    }

    public List<CorePaymentDto> readDriverPayments(Long driverId) {
        final var payments = paymentRepository.findPaymentByTrip_assignedTo_id(driverId);
        final var requestIds = payments.stream()
                .map(Payment::getRequestId)
                .collect(Collectors.toList());

        final var processedPayments = Objects.requireNonNull
                (corePaymentService.getPayments(requestIds).getBody(),
                        "Payment list cannot be null");

        return processedPayments.getPayments();
    }

    private BigDecimal processAuthorizationAmount(BigDecimal tripPrice) {
        return (tripPrice.multiply(tripPrice.divide(BigDecimal.valueOf(100.0f))));
    }
}
