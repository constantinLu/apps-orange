package com.orange.rouber.model;


import com.orange.rouber.client.corepayments.PaymentStatusType;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

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

    private String requestId;

    @OneToOne(mappedBy = "payment", cascade = ALL)
    private Trip trip;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private PaymentStatus paymentStatus;


    public Optional<LocalDateTime> getStartInitiation() {
        return Optional.ofNullable(startInitiation);
    }

    public Optional<LocalDateTime> getEndConfirmation() {
        return Optional.ofNullable(endConfirmation);
    }


    public Payment confirm(LocalDateTime endConfirmation, PaymentStatusType status) {
        return paymentCopy()
                .endConfirmation(endConfirmation)
                .paymentStatus(status.value())
                .build();
    }

    public Payment authorize(PaymentStatusType status) {
        return paymentCopy()
                .paymentStatus(status.value())
                .build();
    }

    private Payment.PaymentBuilder paymentCopy() {
        return Payment.builder()
                .id(this.id)
                .paidPrice(this.paidPrice)
                .trip(this.trip)
                .startInitiation(this.startInitiation)
                .endConfirmation(endConfirmation)
                .requestId(this.requestId)
                .paymentStatus(this.paymentStatus);
    }
}



