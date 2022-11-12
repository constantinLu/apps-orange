package com.orange.rouber.service;

import com.orange.rouber.client.corepayments.CorePaymentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpMethod.PUT;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@RequiredArgsConstructor
@Slf4j
@Service
public class CorePaymentService {

    private static final String CORE_PAYMENTS = "http://localhost:8082/payments";

    private final RestTemplate restTemplate;


    public ResponseEntity<CorePaymentDto> authorizePayment(CorePaymentDto unprocessedPayment) {
        HttpEntity<CorePaymentDto> requestEntity = createRequestEntity(unprocessedPayment);
        return restTemplate.exchange(CORE_PAYMENTS, POST, requestEntity, CorePaymentDto.class);
    }


    public ResponseEntity<CorePaymentDto> confirmPayment(CorePaymentDto authorizedPayment) {
        HttpEntity<CorePaymentDto> requestEntity = createRequestEntity(authorizedPayment);
        return restTemplate.exchange(CORE_PAYMENTS, PUT, requestEntity, CorePaymentDto.class);
    }


    private HttpEntity<CorePaymentDto> createRequestEntity(CorePaymentDto request) {
        return new HttpEntity<>(request, header());
    }

    private HttpHeaders header() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(APPLICATION_JSON));
        headers.setContentType(APPLICATION_JSON);
        return headers;
    }
}
