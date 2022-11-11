package com.orange.corepayments.client;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CorePaymentDto {

    private BigDecimal amount;

    private Optional<String> reason;

    private BigDecimal reward;

    private UUID requestId;

    private PaymentStatusType paymentStatus;

    private LocalDateTime updatedDate;

    private LocalDateTime createdDate;
}
