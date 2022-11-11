package com.orange.corepayments.model;

import com.orange.corepayments.client.PaymentStatusType;
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
