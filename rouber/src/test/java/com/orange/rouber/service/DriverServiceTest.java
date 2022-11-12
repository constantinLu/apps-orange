package com.orange.rouber.service;

import com.orange.rouber.repository.DriverRepository;
import data.DriverTestData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest implements DriverTestData {

    @Mock
    DriverRepository driverRepository;

    @InjectMocks
    DriverService driverService;

    @Test
    void should_process_driver_rating_when_multiple_trips_are_available() {
        //given
        var driver = aDriver().build();

        when(driverRepository.findById(driver.getId())).thenReturn(Optional.of(driver));

        //when
        driverService.processDriverRating(driver.getId());

        //then
        verify(driverRepository).findById(driver.getId());
    }
}