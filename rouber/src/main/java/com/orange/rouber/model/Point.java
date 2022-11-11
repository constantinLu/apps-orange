package com.orange.rouber.model;

import lombok.*;

import javax.persistence.Embeddable;
import java.math.BigDecimal;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Point {

    private BigDecimal x;
    private BigDecimal y;
}
