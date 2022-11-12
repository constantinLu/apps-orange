package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.orange.rouber.model.Vehicle.State;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class VehicleDto {

    @NotNull
    private String name;

    @NotNull
    private String brand;

    @NotNull
    private String vin;

    @NotNull
    private String licensePlate;

    @NotNull
    private String color;

    @NotNull
    private LocalDate registerDate;

    @Nullable
    private LocalDateTime createdDate;

    @Nullable
    private State state;

    //driver
    @NotNull
    private Long ownerId;

}
