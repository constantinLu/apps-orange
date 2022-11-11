package com.orange.rouber.model;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static javax.persistence.CascadeType.ALL;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "payments")
@Entity(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal paidPrice;

    private LocalDateTime startInitiation;

    private LocalDateTime endConfirmation;

    private UUID requestId;

    @OneToOne(mappedBy = "payment", cascade = ALL)
    private Trip trip;


    public Optional<LocalDateTime> getStartInitiation() {
        return Optional.ofNullable(startInitiation);
    }

    public Optional<LocalDateTime> getEndConfirmation() {
        return Optional.ofNullable(endConfirmation);
    }


    public Payment confirmPayment(BigDecimal amount, LocalDateTime endConfirmation) {
        return Payment.builder()
                .id(this.getId())
                .paidPrice(amount)
                .trip(this.getTrip())
                .startInitiation(this.startInitiation)
                .endConfirmation(endConfirmation)
                .requestId(this.getRequestId())
                .build();
    }


}



