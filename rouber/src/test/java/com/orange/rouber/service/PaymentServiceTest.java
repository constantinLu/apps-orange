package com.orange.rouber.service;

import com.orange.rouber.repository.PaymentRepository;
import data.CorePaymentTestData;
import data.DriverTestData;
import data.TripTestData;
import data.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static com.orange.rouber.client.corepayments.PaymentStatusType.PENDING_AUTHORIZATION;
import static com.orange.rouber.client.corepayments.PaymentStatusType.UNPROCESSED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest implements DriverTestData, UserTestData, TripTestData, CorePaymentTestData {

    @Mock
    PaymentRepository paymentRepository;

    @Mock
    CorePaymentService corePaymentService;

    @InjectMocks
    PaymentService paymentService;


    @Test
    void should_create_payment_and_authorize() {
        //given
        var user = aUser().build();
        var driver = aDriver().build();
        var trip = anOngoingTrip(user, driver).build();
        var payment = aPayment()
                .paidPrice(BigDecimal.valueOf(100.00))
                .trip(trip)
                .build();

        when(paymentRepository.save(any())).thenReturn(payment);
        when(corePaymentService.authorizePayment(any())).thenReturn(ResponseEntity.ok(null));

        //when
        paymentService.createPayment(trip);

        //then
        verify(paymentRepository).save(any());
        verify(corePaymentService).authorizePayment(any());
    }

    @Test
    void should_confirm_payment() {
        //given
        var user = aUser().build();
        var driver = aDriver().build();
        var trip = anOngoingTrip(user, driver).build();
        var payment = aPayment()
                .paymentStatus(PENDING_AUTHORIZATION.value())
                .paidPrice(BigDecimal.valueOf(100.00))
                .trip(trip)
                .build();

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));

        //when
        paymentService.confirmPayment(payment.getId());

        //then
        verify(paymentRepository).findById(payment.getId());
        verify(corePaymentService).confirmPayment(any());
    }


    @Test
    void should_throw_exception_when_payment_is_not_valid() {
        //given
        var user = aUser().build();
        var driver = aDriver().build();
        var trip = anOngoingTrip(user, driver).build();
        var payment = aPayment()
                .paidPrice(BigDecimal.valueOf(100.00))
                .paymentStatus(UNPROCESSED.value())
                .trip(trip)
                .build();

        when(paymentRepository.findById(payment.getId())).thenReturn(Optional.of(payment));


        //when
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            paymentService.confirmPayment(payment.getId());
        });

        assertThat(exception.getMessage()).isEqualTo("OriginalPayment must be AUTHORIZED");

        //then
        verify(paymentRepository).findById(payment.getId());
        verify(corePaymentService, times(0)).confirmPayment(any());
    }

}