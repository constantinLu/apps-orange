package com.orange.corepayments.service;

import com.orange.corepayments.client.CorePaymentDto;
import com.orange.corepayments.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import static com.orange.corepayments.converter.Converter.toCorePayment;
import static org.springframework.http.HttpMethod.PUT;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientNotifierService {

    private final RestTemplate restTemplate;

    private static final String ROUBER = "http://localhost:8081/payments/";

    public void notifyTransactionComplete(Payment payment) {
        String url = UriComponentsBuilder.fromHttpUrl(ROUBER)
                .path(payment.getRequestId())
                .encode()
                .toUriString();
        restTemplate.exchange(url, PUT, new HttpEntity<>(toCorePayment(payment)), CorePaymentDto.class);
    }
}
