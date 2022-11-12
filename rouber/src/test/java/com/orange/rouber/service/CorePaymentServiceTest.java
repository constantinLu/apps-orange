package com.orange.rouber.service;

import com.orange.rouber.client.corepayments.CorePaymentDto;
import data.CorePaymentTestData;
import data.PaymentTestData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@ExtendWith(MockitoExtension.class)
class CorePaymentServiceTest implements PaymentTestData, CorePaymentTestData {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CorePaymentService corePaymentService;

    HttpHeaders headers;

    String url = "http://localhost:8082/payments";

    @BeforeEach
    void setUp() {
        headers = new HttpHeaders();
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
    }

    @SneakyThrows
    @Test
    public void should_send_payment_to_authorization() {
        //given
        var corePayment = aCorePaymentDto().build();

        when(restTemplate.exchange(eq(url), eq(POST), eq(new HttpEntity<>(corePayment, headers)), eq(CorePaymentDto.class)))
                .thenReturn(new ResponseEntity<>(CorePaymentDto.builder().build(), HttpStatus.OK));

        //when
        corePaymentService.authorizePayment(corePayment);

        //then
        verify(restTemplate).exchange(eq(url), eq(POST), eq(new HttpEntity<>(corePayment, headers)), eq(CorePaymentDto.class));
    }


    @SneakyThrows
    @Test
    public void should_send_payment_to_confirmation() {
        //given
        var corePayment = aCorePaymentDto().build();

        when(restTemplate.exchange(eq(url), eq(PUT), eq(new HttpEntity<>(corePayment, headers)), eq(CorePaymentDto.class)))
                .thenReturn(new ResponseEntity<>(CorePaymentDto.builder().build(), HttpStatus.OK));

        //when
        corePaymentService.confirmPayment(corePayment);

        //then
        verify(restTemplate).exchange(eq(url), eq(PUT), eq(new HttpEntity<>(corePayment, headers)), eq(CorePaymentDto.class));
    }
}