package com.orange.rouber.client;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TripDto {

    @NotNull
    private BigDecimal price;

    private Float rating;

    @NotNull
    private BigDecimal start_lat;

    @NotNull
    private BigDecimal start_long;

    @NotNull
    private BigDecimal end_lat;

    @NotNull
    private BigDecimal end_long;

    @NotNull
    private Long requestedByUser;

    private Long assignedTo;
}
