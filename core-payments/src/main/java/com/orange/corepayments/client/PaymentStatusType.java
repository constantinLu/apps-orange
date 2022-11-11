package com.orange.corepayments.client;

import java.io.Serializable;

public enum PaymentStatusType implements Serializable {
    UNPROCESSED,
    PENDING_AUTHORIZATION,
    PENDING_CONFIRMATION,
    SUCCEEDED,
    FAILED;
}