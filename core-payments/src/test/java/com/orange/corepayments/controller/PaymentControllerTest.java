package com.orange.corepayments.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.corepayments.client.CorePaymentResponse;
import data.CorePaymentTestData;
import data.PaymentTestData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import setup.CorePaymentsTestSetup;

import java.util.List;
import java.util.UUID;

import static com.orange.corepayments.client.PaymentStatusType.*;
import static com.orange.corepayments.converter.Converter.toCorePaymentDtos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class PaymentControllerTest extends CorePaymentsTestSetup implements PaymentTestData, CorePaymentTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @SneakyThrows
    @Test
    void should_get_list_of_payments_when_request_id_is_present() {
        //given
        var payment = add(aPayment().requestId(UUID.randomUUID().toString()).build());

        final var corePaymentResponse = CorePaymentResponse.builder()
                .payments(toCorePaymentDtos(List.of(payment)))
                .build();

        //when
        var mvcResult = mockMvc.perform(get("/payments")
                        .queryParam("requestIds", payment.getRequestId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();


        //then
        var readValue = objectMapper.readValue(mvcResult, CorePaymentResponse.class);
        assertThat(readValue.getPayments().get(0).getRequestId()).isEqualTo(corePaymentResponse.getPayments().get(0).getRequestId());
    }

    @SneakyThrows
    @Test
    void should_return_empty_when_payment_not_found() {
        //given
        var payment = add(aPayment().requestId(UUID.randomUUID().toString()).build());

        //when
        var mvcResult = mockMvc.perform(get("/payments")
                        .queryParam("requestIds", UUID.randomUUID().toString()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        var paymentResponse = objectMapper.readValue(mvcResult, CorePaymentResponse.class);

        assertThat(paymentResponse.getPayments()).isEmpty();
    }

    @SneakyThrows
    @Test
    void should_authorize_an_unprocessed_payment() {
        //given
        var request = aCorePaymentDto().paymentStatus(UNPROCESSED).build();


        // expect
        var mvcResult = mockMvc.perform(post("/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isAccepted());
    }

    @SneakyThrows
    @Test
    void should_throw_exception_when_payment_is_not_unprocessed() {
        //given
        var authorizedPayment = aCorePaymentDto().build();

        // expect
        var mvcResult = mockMvc.perform(post("/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizedPayment)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof IllegalArgumentException).isTrue())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("Payment can only be UNPROCESSED"));
    }

    @SneakyThrows
    @Test
    void should_confirm_an_authorized_payment() {
        //given
        var aPayment = add(aPayment().build());
        var authorizedPayment = aCorePaymentDto().requestId(aPayment.getRequestId()).paymentStatus(PENDING_AUTHORIZATION).build();

        // expect
        var mvcResult = mockMvc.perform(put("/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizedPayment)))
                .andExpect(status().isAccepted());
    }

    @SneakyThrows
    @Test
    void should_throw_exception_when_payment_is_not_authorized() {
        //given
        var authorizedPayment = aCorePaymentDto().paymentStatus(PENDING_CONFIRMATION).build();


        // expect
        var mvcResult = mockMvc.perform(put("/payments")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authorizedPayment)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertThat(result.getResolvedException() instanceof IllegalArgumentException).isTrue())
                .andExpect(result -> assertThat(result.getResolvedException().getMessage()).isEqualTo("Payment can only be AUTHORIZED"));
    }
}