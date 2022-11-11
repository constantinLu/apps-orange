package com.orange.rouber.client;

import lombok.*;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TripDto {

    private BigDecimal price;

    private Float rating;

    private BigDecimal start_lat;

    private BigDecimal start_long;

    private BigDecimal end_lat;

    private BigDecimal end_long;

    private Long requestedByUser;

    private Long assignedTo;
}
