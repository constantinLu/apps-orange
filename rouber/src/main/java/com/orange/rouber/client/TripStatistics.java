package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TripStatistics {

    BigDecimal totalPricePerDay;

    LocalTime totalTimePerDay;

    BigDecimal avgPricePerDay;

}
