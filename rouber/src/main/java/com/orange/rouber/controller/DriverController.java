package com.orange.rouber.controller;

import com.orange.rouber.client.DriverDto;
import com.orange.rouber.client.DriverProfileDto;
import com.orange.rouber.client.TripInfoDto;
import com.orange.rouber.converter.Converters;
import com.orange.rouber.service.DriverService;
import com.orange.rouber.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.orange.rouber.converter.Converters.*;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    private final TripService tripService;


    @Operation(summary = "Register a driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerDriver(@RequestBody DriverDto driverDto) {
        driverService.registerDriver(toDriver(driverDto));
    }


    @Operation(summary = "GET Driver info List of trip and payment info for each driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/{driverId}/trips")
    public List<TripInfoDto> getDriverTripInfo(@PathVariable Long driverId) {
        final var trips = tripService.getDriverTrips(driverId);
        return trips.stream()
                .map(t -> TripInfoDto.builder()
                        .tripDto(Converters.toTripDto(t))
                        .paymentDto(Converters.toPaymentDto(t.getPayment()))
                        .build())
                .collect(Collectors.toList());
    }

    @Operation(summary = "GET Driver rating. Current rating of the driver")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{driverId}/ratings")
    public DriverDto getDriverRating(@PathVariable Long driverId) {
        final var driver = driverService.getDriver(driverId);
        return DriverDto.builder()
                .rating(driver.getRating())
                .build();
    }


    @Operation(summary = "GET Driver Info Profile")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{driverId}/profile")
    public DriverProfileDto getDriveProfileInfo(@PathVariable Long driverId) {
        final var driver = driverService.getDriver(driverId);
        final var avgPricePerTrip = tripService.calculateAverageTripPrice(driverId);
        return DriverProfileDto.builder()
                .driverDto(toDriverDto(driver))
                .vehicleDto(toVehicleDto(driver.activeVehicle()))
                .averageTripPrice(avgPricePerTrip)
                .build();
    }
}
