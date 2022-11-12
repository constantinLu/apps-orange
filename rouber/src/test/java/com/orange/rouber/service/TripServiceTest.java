package com.orange.rouber.service;

import com.orange.rouber.repository.TripRepository;
import data.DriverTestData;
import data.TripTestData;
import data.UserTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TripServiceTest implements DriverTestData, UserTestData, TripTestData {

    @Mock
    TripRepository tripRepository;

    @InjectMocks
    TripService tripService;

    @Test
    void should_process_driver_rating_when_multiple_trips_are_available() {
        //given
        var now = LocalDate.now();
        var user = aUser().build();
        var driver = aDriver().build();
        var trip1 = aFinalizedTrip(user, driver)
                .price(BigDecimal.valueOf(20))
                .startTrip(LocalDateTime.of(now, LocalTime.of(12, 0, 0)))
                .endTrip(LocalDateTime.of(now, LocalTime.of(13, 0, 0)))
                .build();
        var trip2 = aFinalizedTrip(user, driver)
                .startTrip(LocalDateTime.of(now, LocalTime.of(14, 0, 0)))
                .endTrip(LocalDateTime.of(now, LocalTime.of(15, 0, 0)))
                .build();

        when(tripRepository.findByAssignedTo_IdAndStartTripBetween(any(), any(), any())).thenReturn(List.of(trip1, trip2));


        //when
        var result = tripService.calculateStatistics(driver.getId(), LocalDate.now());

        //then
        assertThat(result.getAvgPricePerDay()).isEqualTo(BigDecimal.valueOf(15.0));
        assertThat(result.getTotalPricePerDay()).isEqualTo(BigDecimal.valueOf(30.0));
        assertThat(result.getTotalTimePerDay()).isEqualTo(LocalTime.of(2, 0, 0));
    }
}