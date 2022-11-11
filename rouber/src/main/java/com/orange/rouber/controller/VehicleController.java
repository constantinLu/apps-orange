package com.orange.rouber.controller;

import com.orange.rouber.client.VehicleDto;
import com.orange.rouber.converter.Converters;
import com.orange.rouber.service.VehicleService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.orange.rouber.converter.Converters.toVehicleDtos;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }


    @ApiOperation(value = "Registers a vehicle", notes = "Vehicle registration")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void registerVehicle(@RequestBody VehicleDto vehicleDto) {
        vehicleService.registerVehicle(Converters.toVehicle(vehicleDto), vehicleDto.getOwnerId());
    }

    @ApiOperation(value = "Gets list of vehicle history", notes = "Gets the list of vehicles used by the driver in the past (INACTIVE)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/history")
    public List<VehicleDto> getVehicleHistory() {
        return toVehicleDtos(vehicleService.getVehicleHistory());
    }
}
