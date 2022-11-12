package com.orange.rouber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.rouber.client.TripDto;
import data.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import setup.RouberTestSetup;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.orange.rouber.client.TripTrigger.START;
import static com.orange.rouber.converter.Converter.toTripDto;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class TripControllerTest extends RouberTestSetup implements VehicleTestData, TripTestData, DriverTestData, PaymentTestData, UserTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @AfterEach
    void afterEach() {
        cleanup();
    }

    @SneakyThrows
    @Test
    void should_create_a_trip() {
        //given
        var user = add(aUser().build());
        var trip = aTrip(user).requestedBy(user).build();
        var tripDto = toTripDto(trip);

        // when
        var mvcResult = mockMvc.perform(post("/trips")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDto)))
                .andExpect(status().isCreated());

        //then
        var savedTrip = get(trip);
        assertThat(savedTrip.getPayment() == null).isTrue();
        assertThat(savedTrip.getPrice().intValue()).isEqualTo(tripDto.getPrice().intValue());
    }


    @SneakyThrows
    @Test
    void should_throw_exception_when_starting_a_trip_and_driver_is_already_assigned() {
        //given
        var user = add(aUser().build());
        var driver = add(aDriver().build());
        var trip = add(aTrip(user).id(1L)
                .requestedBy(user)
                .assignedTo(driver)
                .build());
        var tripDto = TripDto.builder()
                .assignedTo(driver.getId())
                .price(trip.getPrice())
                .build();

        // when
        var mvcResult = mockMvc.perform(put("/trips/1")
                        .queryParam("tripTrigger", START.name())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof ValidationException).isTrue());
    }

    @SneakyThrows
    @Test
    void should_throw_exception_when_starting_a_trip_and_no_trip_is_found() {
        //given
        var user = add(aUser().build());
        var driver = add(aDriver().build());
        var trip = aTrip(user).id(1L) //not added to the db
                .requestedBy(user)
                .assignedTo(driver)
                .build();
        var tripDto = toTripDto(trip);

        // when
        var mvcResult = mockMvc.perform(put("/trips/1")
                        .queryParam("tripTrigger", START.name())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDto)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof ValidationException).isTrue())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("No trip available"));
    }

    @SneakyThrows
    @Test
    void should_get_trip_statistics_when_date_is_provided() {
        //given
        var user = add(aUser().build());
        var driver = add(aDriver().id(1L).build());
        var trip = add(aTrip(user).id(1L)
                .requestedBy(user)
                .assignedTo(driver)
                .startTrip(LocalDateTime.now().minus(50, MINUTES))
                .endTrip(LocalDateTime.now().minus(10, MINUTES))
                .build());
        var tripDto = toTripDto(trip);

        // when
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/trips/drivers/1/statistics")
                        .queryParam("date", LocalDate.now().toString())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDto)))
                .andExpect(status().isOk());

        //then
        var savedTrip = get(trip);
        assertThat(savedTrip.getEndTrip() != null).isTrue();
    }


    @SneakyThrows
    @Test
    void should_rate_trip_and_calculate_driver_rating() {
        //given
        var user = add(aUser().build());
        var driver = add(aDriver().id(1L).build());
        var trip1 = add(aTrip(user).id(1L)
                .requestedBy(user)
                .rating(2f)
                .assignedTo(driver)
                .startTrip(LocalDateTime.now().minus(50, MINUTES))
                .endTrip(LocalDateTime.now().minus(10, MINUTES))
                .build());
        var trip2 = add(aTrip(user).id(2L)
                .requestedBy(user)
                .rating(4f)
                .assignedTo(driver)
                .startTrip(LocalDateTime.now().minus(50, MINUTES))
                .endTrip(LocalDateTime.now().minus(10, MINUTES))
                .build());
        var tripDto = toTripDto(trip1);

        // when
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.put("/trips/1/ratings")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(tripDto)))
                .andExpect(status().isOk());

        //then
        var driverRating = get(driver).getRating();

        assertThat(driverRating).isEqualTo(3f);
    }

}


