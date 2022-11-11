package com.orange.rouber.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserDto {

    @NotNull
    private String name;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Long phoneNumber;

    @NotNull
    private String address;

}
