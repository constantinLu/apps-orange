package com.orange.corepayments.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CorePaymentDto {

    private BigDecimal amount;

    private Optional<String> reason;

    private BigDecimal reward;

    private String requestId;

    private PaymentStatusType paymentStatus;

    private LocalDateTime updatedDate;

    private LocalDateTime createdDate;
}
