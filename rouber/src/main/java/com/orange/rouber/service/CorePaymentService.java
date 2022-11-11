package com.orange.rouber.service;

import com.orange.rouber.client.corepayments.CorePaymentDto;
import com.orange.rouber.client.corepayments.CorePaymentResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@Service
public class CorePaymentService {

    private static final String CORE_PAYMENTS = "http://localhost:8082/payments";

    private final RestTemplate restTemplate;

    public CorePaymentService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public ResponseEntity<CorePaymentDto> authorizePayment(CorePaymentDto unprocessedPayment) {
        HttpEntity<CorePaymentDto> requestEntity = createRequestEntity(unprocessedPayment);
        return restTemplate.exchange(CORE_PAYMENTS, POST, requestEntity, CorePaymentDto.class);
    }


    public ResponseEntity<CorePaymentDto> confirmPayment(CorePaymentDto authorizedPayment) {
        HttpEntity<CorePaymentDto> requestEntity = createRequestEntity(authorizedPayment);
        return restTemplate.exchange(CORE_PAYMENTS, PUT, requestEntity, CorePaymentDto.class);
    }

    public ResponseEntity<CorePaymentResponse> getPayments(List<UUID> requestIds) {
        final var httpEntity = new HttpEntity<>(header());
        String url = UriComponentsBuilder.fromHttpUrl(CORE_PAYMENTS)
                .queryParam("requestIds", "{requestIds}")
                .encode()
                .toUriString();

        Map<String, List<UUID>> params = new HashMap<>();
        params.put("requestIds", requestIds);

        return restTemplate.exchange(url, GET, httpEntity, CorePaymentResponse.class, params);
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
