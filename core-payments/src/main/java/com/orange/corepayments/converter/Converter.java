package com.orange.corepayments.converter;

import com.orange.corepayments.client.CorePaymentDto;
import com.orange.corepayments.client.PaymentDto;
import com.orange.corepayments.model.Payment;
import com.orange.corepayments.model.PaymentStatus;

import java.util.List;
import java.util.stream.Collectors;

public class Converter {

    public static Payment toPayment(PaymentDto paymentDto) {
        return Payment.builder()
                .amount(paymentDto.getAmount())
                .reward(paymentDto.getReward())
                .reason(paymentDto.getReason().orElse(null))
                .requestId(paymentDto.getRequestId())
                .paymentStatus(PaymentStatus.builder()
                        .type(paymentDto.getPaymentStatus())
                        .build())
                .build();
    }

    public static Payment toPayment(CorePaymentDto paymentDto) {
        return Payment.builder()
                .amount(paymentDto.getAmount())
                .reward(paymentDto.getReward())
                .reason(paymentDto.getReason().orElse(null))
                .requestId(paymentDto.getRequestId())
                .paymentStatus(PaymentStatus.builder()
                        .type(paymentDto.getPaymentStatus())
                        .build())
                .build();
    }

    public static PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .amount(payment.getAmount())
                .reward(payment.getReward())
                .reason(payment.getReason())
                .requestId(payment.getRequestId())
                .paymentStatus(payment.getPaymentStatus().getType())
                .createdDate(payment.getCreatedDate())
                .updatedDate(payment.getUpdatedDate())
                .build();
    }

    public static CorePaymentDto toCorePayment(Payment payment) {
        return CorePaymentDto.builder()
                .amount(payment.getAmount())
                .reward(payment.getReward())
                .reason(payment.getReason())
                .requestId(payment.getRequestId())
                .paymentStatus(payment.getPaymentStatus().getType())
                .createdDate(payment.getCreatedDate())
                .updatedDate(payment.getUpdatedDate())
                .build();
    }

    public static List<CorePaymentDto> toCorePaymentDtos(List<Payment> payments) {
        return payments.stream()
                .map(Converter::toCorePayment)
                .collect(Collectors.toList());
    }
}
