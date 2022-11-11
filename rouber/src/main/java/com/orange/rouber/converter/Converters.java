package com.orange.rouber.converter;

import com.orange.rouber.client.*;
import com.orange.rouber.model.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class Converters {

    public static Driver toDriver(DriverDto driverDto) {
        return Driver.builder()
                .name(driverDto.getName())
                .email(driverDto.getEmail())
                .phoneNumber(driverDto.getPhoneNumber())
                .rating(driverDto.getRating().orElse(0F))
                .build();
    }

    public static DriverDto toDriverDto(Driver driver) {
        return DriverDto.builder()
                .name(driver.getName())
                .email(driver.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .phoneNumber(userDto.getPhoneNumber())
                .address(userDto.getAddress())
                .build();
    }

    public static Trip toTrip(TripDto tripDto) {
        return Trip.builder()
                .price(tripDto.getPrice())
                .rating(tripDto.getRating().floatValue())
                .startLocation(coordinates(tripDto.getStart_lat(), tripDto.getStart_long()))
                .endLocation(coordinates(tripDto.getEnd_lat(), tripDto.getEnd_long()))
                .build();
    }

    public static Vehicle toVehicle(VehicleDto vehicleDto) {
        return Vehicle.builder()
                .name(vehicleDto.getName())
                .brand(vehicleDto.getBrand())
                .vin(vehicleDto.getVin())
                .licensePlate(vehicleDto.getLicensePlate())
                .color(vehicleDto.getColor())
                .registerDate(vehicleDto.getRegisterDate())
                .createdDate(LocalDateTime.now())
                .build();
    }


    public static TripDto toTripDto(Trip trip) {
        return TripDto.builder()
                .price(trip.getPrice())
                .rating(trip.getRating())
                .start_lat(trip.getStartLocation().getX())
                .start_long(trip.getStartLocation().getY())
                .end_lat(trip.getEndLocation().getX())
                .end_long(trip.getEndLocation().getY())
                .requestedByUser(trip.getRequestedBy().getId())
                .assignedTo(trip.getAssignedTo().getId())
                .build();
    }

    public static List<TripDto> toTripDtos(List<Trip> trips) {
        return trips.stream()
                .map(Converters::toTripDto)
                .collect(Collectors.toList());
    }


    public static VehicleDto toVehicleDto(Vehicle vehicle) {
        return VehicleDto.builder()
                .name(vehicle.getName())
                .brand(vehicle.getBrand())
                .vin(vehicle.getVin())
                .licensePlate(vehicle.getLicensePlate())
                .color(vehicle.getColor())
                .registerDate(vehicle.getRegisterDate())
                .createdDate(vehicle.getCreatedDate())
                .state(vehicle.getState())
                .build();
    }

    public static PaymentDto toPaymentDto(Payment payment) {
        return PaymentDto.builder()
                .paidPrice(payment.getPaidPrice())
                .startInitiation(payment.getStartInitiation().orElse(null))
                .endConfirmation(payment.getEndConfirmation().orElse(null))
                .requestId(payment.getRequestId())
                .build();
    }

    public static List<VehicleDto> toVehicleDtos(List<Vehicle> vehicles) {
        return vehicles.stream()
                .map(Converters::toVehicleDto)
                .collect(Collectors.toList());
    }

    private static Point coordinates(BigDecimal latitude, BigDecimal longitude) {
        return Point.builder()
                .x(latitude)
                .y(longitude)
                .build();
    }
}
