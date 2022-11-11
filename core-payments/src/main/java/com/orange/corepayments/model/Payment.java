package com.orange.corepayments.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;
import static com.orange.corepayments.client.PaymentStatusType.PENDING_CONFIRMATION;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Table(name = "payments")
@Entity(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Column
    private String reason;

    private BigDecimal reward;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "status_id")
    private PaymentStatus paymentStatus;

    LocalDateTime updatedDate;

    LocalDateTime createdDate;

    private UUID requestId;


    public Optional<String> getReason() {
        return Optional.ofNullable(reason);
    }

    public Payment authorizePayment(BigDecimal amount, UUID requestId) {
        return Payment.builder()
                .amount(amount)
                .requestId(requestId)
                .createdDate(LocalDateTime.now())
                .paymentStatus(PaymentStatus.builder()
                        .type(PENDING_AUTHORIZATION)
                        .build())
                .reason(this.getReason().orElse(null))
                .reward(this.getReward())
                .build();
    }

    public Payment confirmPayment(Payment incomingPayment) {
        return Payment.builder()
                .id(this.getId())
                .amount(incomingPayment.getAmount())
                .requestId(incomingPayment.getRequestId())
                .paymentStatus(PaymentStatus.builder()
                        .type(PENDING_CONFIRMATION)
                        .build())
                .reason(this.getReason().orElse(null))
                .updatedDate(LocalDateTime.now())
                .createdDate(this.getCreatedDate())
                .reward(this.getReward())
                .build();
    }


}



