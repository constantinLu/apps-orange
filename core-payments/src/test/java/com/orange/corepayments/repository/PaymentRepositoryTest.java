package com.orange.corepayments.repository;

import data.PaymentTestData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import setup.CorePaymentsTestSetup;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PaymentRepositoryTest extends CorePaymentsTestSetup implements PaymentTestData {


    @Test
    void should_find_request_by_id() {
        //given
        var requestId = UUID.randomUUID().toString();
        var payment = paymentRepository.save(aPayment().requestId(requestId).build());

        //when
        var result = paymentRepository.findByRequestId(payment.getRequestId());

        //then
        assertThat(result.getRequestId().equals(requestId));
    }

    @Test
    void should_find_by_request_id_in() {
        var payment1 = paymentRepository.save(aPayment().requestId(UUID.randomUUID().toString()).build());
        var payment2 = paymentRepository.save(aPayment().requestId(UUID.randomUUID().toString()).build());
        var requestIds = List.of(payment1.getRequestId(), payment2.getRequestId());
        //when

        var result = paymentRepository.findByRequestIdIn(requestIds);

        //then
        assertThat(List.of(payment1, payment2)).hasSameElementsAs(result);
    }
}