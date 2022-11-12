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

import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.HOURS;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TripRepositoryTest extends RouberTestSetup implements PaymentTestData, DriverTestData, TripTestData, UserTestData {


    @Test
    void should_by_driver_id() {
        //given
        var user = add(aUser().build());
        var driver = add(aDriver().build());
        var payment = add(aPayment().build());
        var trip = add(aTrip(user).payment(payment).assignedTo(driver).build());

        //when
        var result = tripRepository.findByAssignedTo_Id(driver.getId());

        //then
        assertThat(result.get(0)).isEqualTo(trip);
    }

    @Test
    void should_new_trip_when_driver_is_not_assigned() {
        //given
        var user = add(aUser().build());
        var payment = add(aPayment().build());
        var trip = add(aTrip(user).payment(payment).build());

        //when
        var result = tripRepository.findByAssignedTo_IdIsNull();

        //then
        assertThat(result.get(0)).isEqualTo(trip);
    }

    @Test
    void should_find_trip_after_start_date() {
        //given
        var user = add(aUser().build());
        var payment = add(aPayment().build());
        var driver = add(aDriver().build());
        final var now = LocalDateTime.now();
        var trip = add(aTrip(user).id(1L)
                .requestedBy(user)
                .assignedTo(driver)
                .startTrip(now.minus(50, MINUTES))
                .endTrip(now.minus(10, MINUTES))
                .build());

        //when
        var result = tripRepository.findByAssignedTo_IdAndStartTripBetween(driver.getId(), now.minus(2, HOURS), now);

        //then
        assertThat(result.get(0)).isEqualTo(trip);
    }
}