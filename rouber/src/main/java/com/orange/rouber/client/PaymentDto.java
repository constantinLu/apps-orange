package com.orange.rouber.client;


import com.orange.rouber.client.corepayments.PaymentStatusType;
import com.orange.rouber.model.Trip;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaymentDto {

    @NotNull
    private BigDecimal paidPrice;

    private LocalDateTime startInitiation;

    private LocalDateTime endConfirmation;

    private Trip trip;

    @NotNull
    private String requestId;

    @NotNull
    private PaymentStatusType status;

    public Optional<LocalDateTime> getStartInitiation() {
        return Optional.ofNullable(startInitiation);
    }

    public Optional<LocalDateTime> getEndConfirmation() {
        return Optional.ofNullable(endConfirmation);
    }
}



