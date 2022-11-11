package com.orange.rouber.client;


import com.orange.rouber.model.Trip;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class PaymentDto {

    private BigDecimal paidPrice;

    private LocalDateTime startInitiation;

    private LocalDateTime endConfirmation;

    private Trip trip;

    private UUID requestId;

    public Optional<LocalDateTime> getStartInitiation() {
        return Optional.ofNullable(startInitiation);
    }

    public Optional<LocalDateTime> getEndConfirmation() {
        return Optional.ofNullable(endConfirmation);
    }
}



