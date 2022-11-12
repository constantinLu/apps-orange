package com.orange.rouber.client.corepayments;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CorePaymentDto implements Serializable {

    @NotNull
    BigDecimal amount;

    Optional<String> reason;

    BigDecimal reward;

    @NotNull
    String requestId;

    @NotNull
    PaymentStatusType paymentStatus;

    LocalDateTime updatedDate;

    LocalDateTime createdDate;
}
