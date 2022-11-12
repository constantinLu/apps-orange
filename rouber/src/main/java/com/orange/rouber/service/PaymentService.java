package com.orange.rouber.service;

import com.orange.rouber.client.corepayments.CorePaymentDto;
import com.orange.rouber.model.Payment;
import com.orange.rouber.model.Trip;
import com.orange.rouber.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.orange.rouber.client.corepayments.PaymentStatusType.*;

@RequiredArgsConstructor
@Slf4j
@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    private final CorePaymentService corePaymentService;

    public List<Payment> readDriverPayments(Long driverId) {
        return paymentRepository.findPaymentByTrip_assignedTo_id(driverId);
    }

    public Payment createPayment(Trip trip) {
        final var smallAmount = processAuthorizationAmount(trip.getPrice());
        final var requestId = UUID.randomUUID().toString();

        authorizePayment(smallAmount, requestId);

        final var payment = Payment.builder()
                .paidPrice(trip.getPrice())
                .trip(trip)
                .startInitiation(LocalDateTime.now()) //start of init
                .requestId(requestId)
                .paymentStatus(UNPROCESSED.value())
                .build();

        return paymentRepository.save(payment);
    }

    public void confirmPayment(Long paymentId) {
        final var originalPayment = paymentRepository.findById(paymentId).orElseThrow();
        validatePayment(originalPayment);

        final var authorizedPaymentRequest = CorePaymentDto.builder()
                .amount(originalPayment.getTrip().getPrice())
                .requestId(originalPayment.getRequestId())
                .createdDate(originalPayment.getStartInitiation().get())
                .paymentStatus(PENDING_AUTHORIZATION)
                .reason(Optional.of("Trip ended. Confirmation of payment required"))
                .build();

        corePaymentService.confirmPayment(authorizedPaymentRequest);
    }

    public void updatePaymentStatus(String requestId, CorePaymentDto incomingPayment) {
        final var originalPayment = paymentRepository.findByRequestId(requestId);
        switch (incomingPayment.getPaymentStatus()) {
            case PENDING_AUTHORIZATION:
                updateToAuthorized(originalPayment, incomingPayment);
                break;
            case PENDING_CONFIRMATION:
                updateToConfirmed(originalPayment, incomingPayment);
                break;
            default:
                throw new IllegalArgumentException("Status not supported" + incomingPayment.getPaymentStatus());
        }
    }

    private void updateToAuthorized(Payment originalPayment, CorePaymentDto incomingPayment) {
        Assert.isTrue(originalPayment.getPaymentStatus().getType().equals(UNPROCESSED), "Original payment must be UNPROCESSED");
        Assert.isTrue(incomingPayment.getPaymentStatus().equals(PENDING_AUTHORIZATION), "Incoming payment must be AUTHORIZED");
        Assert.isTrue(originalPayment.getStartInitiation().isPresent(), "Init date must be present");
        paymentRepository.save(originalPayment.authorize(incomingPayment.getPaymentStatus()));
    }

    private void updateToConfirmed(Payment originalPayment, CorePaymentDto incomingPayment) {
        Assert.isTrue(originalPayment.getPaymentStatus().getType().equals(PENDING_AUTHORIZATION), "Original payment must be AUTHORIZED");
        Assert.isTrue(originalPayment.getStartInitiation().isPresent(), "Init date must be present");

        Assert.isTrue(incomingPayment.getUpdatedDate() != null, "End date must be present");
        Assert.isTrue(incomingPayment.getPaymentStatus().equals(PENDING_CONFIRMATION), "Incoming payment must be CONFIRMED");
        paymentRepository.save(originalPayment.confirm(incomingPayment.getUpdatedDate(), incomingPayment.getPaymentStatus()));
    }


    private BigDecimal processAuthorizationAmount(BigDecimal tripPrice) {
        return (tripPrice.multiply(tripPrice.divide(BigDecimal.valueOf(100.0f))));
    }

    private void authorizePayment(BigDecimal amount, String requestId) {
        final var unProcessedPayment = CorePaymentDto.builder()
                .amount(amount)
                .requestId(requestId)
                .paymentStatus(UNPROCESSED)
                .reason(Optional.of("Start of trip authorization"))
                .build();

        corePaymentService.authorizePayment(unProcessedPayment);
    }

    private void validatePayment(Payment originalPayment) {
        Assert.isTrue(originalPayment.getStartInitiation().isPresent(), "Start init must be present");
        Assert.isTrue(originalPayment.getPaymentStatus().getType().equals(PENDING_AUTHORIZATION), "OriginalPayment must be AUTHORIZED");
        Assert.isTrue(originalPayment.getEndConfirmation().isEmpty(), "Payment should not be CONFIRMED YET");
    }
}
