package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class DriverDto {


    private String name;

    @Email
    @NotNull
    private String email;

    private Long phoneNumber;

    @Min(0)
    @Max(5)
    private Float rating;

    public Optional<Float> getRating() {
        return Optional.ofNullable(rating);
    }

    public Float rating() {
        return rating;
    }
}
