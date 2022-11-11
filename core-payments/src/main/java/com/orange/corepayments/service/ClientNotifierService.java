package com.orange.corepayments.service;

import com.orange.corepayments.model.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientNotifierService {

    private final RestTemplate restTemplate;

    public void notifyTransactionComplete(String callbackUrl, Payment payment) {
        restTemplate.exchange(callbackUrl, HttpMethod.POST, new HttpEntity<>(payment), Payment.class);
    }
}
