package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class TripInfoDto {

    TripDto tripDto;

    PaymentDto paymentDto;
}
