package com.orange.rouber.model;

import com.orange.rouber.client.corepayments.PaymentStatusType;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import static javax.persistence.EnumType.STRING;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "payments_status")
public class PaymentStatus {

    @Id
    @Enumerated(STRING)
    private PaymentStatusType type;
}
