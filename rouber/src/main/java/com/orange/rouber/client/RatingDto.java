package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class RatingDto {

    private Float rating;

    private Optional<Long> driverId;

    private Optional<Long> tripId;
}
