package com.orange.rouber.controller;

import com.orange.rouber.client.VehicleDto;
import com.orange.rouber.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.orange.rouber.converter.Converter.toVehicle;
import static com.orange.rouber.converter.Converter.toVehicleDtos;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;


    @Operation(summary = "Registers a vehicle. Vehicle registration")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerVehicle(@Valid @RequestBody VehicleDto vehicleDto) {
        vehicleService.registerVehicle(toVehicle(vehicleDto), vehicleDto.getOwnerId());
    }

    @GetMapping("/history")
    public List<VehicleDto> getVehicleHistory() {
        return toVehicleDtos(vehicleService.getVehicleHistory());
    }
}
