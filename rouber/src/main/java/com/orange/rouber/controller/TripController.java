package com.orange.rouber.controller;

import com.orange.rouber.client.TripDto;
import com.orange.rouber.client.TripStatistics;
import com.orange.rouber.client.TripTrigger;
import com.orange.rouber.service.TripService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.util.List;

import static com.orange.rouber.converter.Converters.toTrip;
import static com.orange.rouber.converter.Converters.toTripDtos;

@RestController
@RequestMapping("/trips")
public class TripController {

    TripService tripService;

    public TripController(TripService tripService) {
        this.tripService = tripService;
    }


    @ApiOperation(value = "All available trips", notes = "Get the list of available trips")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping()
    public List<TripDto> readAvailableTrips() {
        return toTripDtos(tripService.readAvailableTrips());
    }


    @ApiOperation(value = "Driver list of trips", notes = "Get the list of driver trips")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/drivers/{driverId}")
    public List<TripDto> getDriverTrips(@PathVariable Long driverId) {
        return toTripDtos(tripService.getDriverTrips(driverId));
    }


    @ApiOperation(value = "Driver rating by trip", notes = "Get the driver rating by trip")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("{tripId}/ratings")
    public TripDto getDriverRatings(@PathVariable Long tripId, @RequestBody TripDto tripDto) {
        Assert.notNull(tripDto.getAssignedTo(), "Driver ID: cannot be null");
        final var trip = tripService.getDriverRatingsByTrip(tripId, tripDto.getAssignedTo());
        return TripDto.builder()
                .rating(trip.getRating())
                .build();
    }


    @ApiOperation(value = "Creates a trip", notes = "First driver that acquire a trip")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void createTrip(@RequestBody TripDto tripDto) {
        assert tripDto.getRequestedByUser() != null;
        tripService.createTrip(toTrip(tripDto), tripDto.getRequestedByUser());
    }


    @ApiOperation(value = "Creates a trip", notes = "Used for triggering the start of the trip and the end. " +
            "Based on request param: START/STOP values")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
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


    @ApiOperation(value = "Rate trip of the driver", notes = "Rate the trip by the client (user)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PutMapping("/{tripId}/ratings")
    public void rateTrip(@PathVariable Long tripId, @RequestBody TripDto tripDto) {
        Assert.notNull(tripDto.getRating(), "Rating must be present");
        Assert.notNull(tripDto.getAssignedTo(), "Driver ID must be present");

        tripService.rateTrip(tripId, tripDto.getAssignedTo(), tripDto.getRating());
    }


    @ApiOperation(value = "GET driver statistics", notes = "Gets the statistics for each driver" +
            "1.total price per day/ 2.total time per day / 3.average price per day")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = TripStatistics.class),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/drivers/{driverId}/statistics")
    public TripStatistics getStatistics(@PathVariable Long driverId, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date) {
        final var now = LocalDate.now();
        Assert.isTrue(date.isBefore(LocalDate.now()) || date.isEqual(now), "Date must not be in the future");
        return tripService.calculateStatistics(driverId, date);
    }
}


