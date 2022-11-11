package com.orange.rouber.client.corepayments;

import com.orange.rouber.model.PaymentStatus;

import java.io.Serializable;

public enum PaymentStatusType implements Serializable {
    UNPROCESSED,
    PENDING_AUTHORIZATION,
    PENDING_CONFIRMATION,
    SUCCEEDED,
    FAILED;

    public PaymentStatus value() {
        return PaymentStatus.builder()
                .type(PaymentStatusType.valueOf(this.name()))
                .build();
    }
}