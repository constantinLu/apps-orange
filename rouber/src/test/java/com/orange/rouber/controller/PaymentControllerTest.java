package com.orange.rouber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.web.bind.MethodArgumentNotValidException;
import setup.RouberTestSetup;

import java.time.LocalDateTime;
import java.util.List;

import static com.orange.rouber.client.corepayments.PaymentStatusType.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PaymentControllerTest extends RouberTestSetup implements CorePaymentTestData, PaymentTestData, DriverTestData,
        UserTestData, TripTestData {

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
    void should_update_payment_status_to_authorized() {
        //given
        add(aPayment().requestId("3424-434245-43242").paymentStatus(UNPROCESSED.value()).build());
        var corePayment = aCorePaymentDto()
                .requestId("3424-434245-43242")
                .createdDate(LocalDateTime.now())
                .paymentStatus(PENDING_AUTHORIZATION)
                .build();

        // when
        var mvcResult = mockMvc.perform(put("/payments/3424-434245-43242")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(corePayment)))
                .andExpect(status().isOk());

        //then
        var updatedPayment = get(corePayment.getRequestId());

        assertThat(updatedPayment.getPaymentStatus().getType()).isEqualTo(PENDING_AUTHORIZATION);
    }

    @SneakyThrows
    @Test
    void should_update_payment_status_to_confirmed() {
        //given
        add(aPayment().requestId("3424-434245-43242").paymentStatus(PENDING_AUTHORIZATION.value()).build());
        var corePayment = aCorePaymentDto()
                .requestId("3424-434245-43242")
                .updatedDate(LocalDateTime.now())
                .paymentStatus(PENDING_CONFIRMATION)
                .build();

        // when
        var mvcResult = mockMvc.perform(put("/payments/3424-434245-43242")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(corePayment)))
                .andExpect(status().isOk());

        //then
        var updatedPayment = get(corePayment.getRequestId());

        assertThat(updatedPayment.getPaymentStatus().getType()).isEqualTo(PENDING_CONFIRMATION);
    }


    @SneakyThrows
    @Test
    void should_throw_exception_when_request_fails_validation() {
        //given
        var corePayment = aCorePaymentDto().requestId(null).paymentStatus(PENDING_CONFIRMATION).build();

        // expect
        var mvcResult = mockMvc.perform(post("/drivers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(corePayment)))
                .andExpect(result -> assertThat(result.getResolvedException()
                        instanceof MethodArgumentNotValidException).isTrue());
    }


    @SneakyThrows
    @Test
    void should_get_list_of_payments() {
        //given
        var user = add(aUser().build());
        var driver = add(aDriver().build());
        var payment1 = add(aPayment().build());
        var payment2 = add(aPayment().build());
        var trip1 = add(aTrip(user).assignedTo(driver).payment(payment1).build());
        var trip2 = add(aTrip(user).assignedTo(driver).payment(payment2).build());

        //when
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/payments/driver/1"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var paymentResult = getAll(List.of(payment1.getRequestId(), payment2.getRequestId()));
        //then
        assertThat(paymentResult.size()).isEqualTo(2);
    }

}