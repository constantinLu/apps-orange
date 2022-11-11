package com.orange.rouber.controller;

import com.orange.rouber.client.DriverDto;
import com.orange.rouber.client.DriverProfileDto;
import com.orange.rouber.client.TripInfoDto;
import com.orange.rouber.converter.Converters;
import com.orange.rouber.service.DriverService;
import com.orange.rouber.service.TripService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.orange.rouber.converter.Converters.*;

@RestController
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    private final TripService tripService;

    public DriverController(DriverService driverService, TripService tripService) {
        this.driverService = driverService;
        this.tripService = tripService;
    }

    @ApiOperation(value = "Register a driver")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerDriver(@RequestBody DriverDto driverDto) {
        driverService.registerDriver(toDriver(driverDto));
    }


    @ApiOperation(value = "GET Driver info", notes = "List of trip and payment info for each driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
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

    @ApiOperation(value = "GET Driver rating", notes = " Current rating of the driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = DriverDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("{driverId}/ratings")
    public DriverDto getDriverRating(@PathVariable Long driverId) {
        final var driver = driverService.getDriver(driverId);
        return DriverDto.builder()
                .rating(driver.getRating())
                .build();
    }

    @ApiOperation(value = "GET Driver profile info", notes = "Returns driver data, vehicle data and average trip price")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = DriverProfileDto.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
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
