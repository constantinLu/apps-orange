package com.orange.rouber.client.corepayments;

import java.io.Serializable;

public enum PaymentStatusType implements Serializable {
    UNPROCESSED,
    PENDING_AUTHORIZATION,
    PENDING_CONFIRMATION,
    SUCCEEDED,
    FAILED;
}