package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DriverProfileDto {

    private DriverDto driverDto;

    private VehicleDto vehicleDto;

    private BigDecimal averageTripPrice;
}
