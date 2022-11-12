package com.orange.corepayments.service;

import com.orange.corepayments.client.CorePaymentDto;
import data.PaymentTestData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpMethod.PUT;

@ExtendWith(MockitoExtension.class)
public class ClientNotifierServiceTest implements PaymentTestData {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ClientNotifierService clientNotifierService;

    @SneakyThrows
    @Test
    public void should_notify_transaction_complete() {
        //given
        var payment = aPayment().build();

        var url = UriComponentsBuilder.fromHttpUrl("http://localhost:8081/payments/")
                .path(payment.getRequestId()).encode().toUriString();
        when(restTemplate.exchange(eq(url), eq(PUT), any(), eq(CorePaymentDto.class)))
                .thenReturn(new ResponseEntity<>(CorePaymentDto.builder().build(), HttpStatus.OK));

        //when
        clientNotifierService.notifyTransactionComplete(payment);


        //then
        verify(restTemplate).exchange(eq(url), eq(PUT), any(), eq(CorePaymentDto.class));
    }
}