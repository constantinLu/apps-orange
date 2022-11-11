package com.orange.rouber.controller;

import com.orange.rouber.client.TripDto;
import com.orange.rouber.client.TripStatistics;
import com.orange.rouber.client.TripTrigger;
import com.orange.rouber.service.TripService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

import static com.orange.rouber.converter.Converters.toTrip;
import static com.orange.rouber.converter.Converters.toTripDtos;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/trips")
public class TripController {

    TripService tripService;


    @Operation(summary = "All available trips. Get the list of available trips")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping()
    public List<TripDto> readAvailableTrips() {
        return toTripDtos(tripService.readAvailableTrips());
    }


    @Operation(summary = "Driver list of trips. Get the list of driver trips")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/drivers/{driverId}")
    public List<TripDto> getDriverTrips(@PathVariable Long driverId) {
        return toTripDtos(tripService.getDriverTrips(driverId));
    }


    @Operation(summary = "Driver rating by trip. Get the driver rating by trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("{tripId}/ratings")
    public TripDto getDriverRatings(@PathVariable Long tripId, @RequestBody TripDto tripDto) {
        Assert.notNull(tripDto.getAssignedTo(), "Driver ID: cannot be null");
        final var trip = tripService.getDriverRatingsByTrip(tripId, tripDto.getAssignedTo());
        return TripDto.builder()
                .rating(trip.getRating())
                .build();
    }


    @Operation(summary = "Creates a trip. First driver that acquire a trip")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrip(@RequestBody TripDto tripDto) {
        assert tripDto.getRequestedByUser() != null;
        tripService.createTrip(toTrip(tripDto), tripDto.getRequestedByUser());
    }


    @Operation(summary = "Updates a trip with driver id. Used for triggering the start of the trip and end of the trip. " +
            "Based on request param: START/STOP values")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{tripId}")
    public void triggerTrip(@PathVariable Long tripId, @RequestBody TripDto tripDto, @RequestParam TripTrigger tripTrigger) {
        switch (tripTrigger) {
            case START:
                tripService.startTrip(tripId, tripDto.getAssignedTo());
                break;
            case STOP:
                tripService.endTrip(tripId, tripDto.getAssignedTo());
                break;
            default:
                throw new ValidationException("Parameter not supported!");
        }
    }


    @Operation(summary = "Rate trip of the driver. Rate the trip by the client (user)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{tripId}/ratings")
    public void rateTrip(@PathVariable Long tripId, @RequestBody TripDto tripDto) {
        Assert.notNull(tripDto.getRating(), "Rating must be present");
        Assert.notNull(tripDto.getAssignedTo(), "Driver ID must be present");

        tripService.rateTrip(tripId, tripDto.getAssignedTo(), tripDto.getRating());
    }


    @Operation(summary = "GET driver statistics Gets the statistics for each driver" +
            "1.total price per day/ 2.total time per day / 3.average price per day")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping("/drivers/{driverId}/statistics")
    public TripStatistics getStatistics(@PathVariable Long driverId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        final var now = LocalDate.now();
        Assert.isTrue(date.isBefore(LocalDate.now()) || date.isEqual(now), "Date must not be in the future");
        return tripService.calculateStatistics(driverId, date);
    }
}


