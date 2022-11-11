package com.orange.corepayments.repository;

import data.PaymentTestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase
class PaymentRepositoryTest implements PaymentTestData {

    @Autowired
    PaymentRepository repo;

    @Test
    void should_find_request_by_id() {
        //given
        var requestId = UUID.randomUUID();
        var payment = repo.save(aPayment().requestId(requestId).build());

        //when
        var result = repo.findByRequestId(requestId);

        //then
        Assertions.assertEquals(result.getRequestId(), requestId);

    }

    @Test
    void findByRequestIdIn() {
    }
}