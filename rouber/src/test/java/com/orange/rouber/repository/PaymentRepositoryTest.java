package com.orange.rouber.repository;

import data.DriverTestData;
import data.PaymentTestData;
import data.TripTestData;
import data.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import setup.RouberTestSetup;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PaymentRepositoryTest extends RouberTestSetup implements PaymentTestData, DriverTestData, TripTestData, UserTestData {

    @Test
    void should_by_driver_id() {
        //given
        var user = userRepository.save(aUser().build());
        var driver = driverRepository.save(aDriver().build());
        var payment = paymentRepository.save(aPayment().build());
        var trip = tripRepository.save(aTrip(user).payment(payment).build());

        //when
        var result = paymentRepository.findPaymentByTrip_assignedTo_id(driver.getId());

        //then
        assertThat(result.equals(List.of(payment)));
    }
}