package com.orange.rouber.client.corepayments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CorePaymentDto implements Serializable {

    BigDecimal amount;

    Optional<String> reason;

    BigDecimal reward;

    UUID requestId;

    PaymentStatusType paymentStatus;

    LocalDateTime updatedDate;

    LocalDateTime createdDate;
}
