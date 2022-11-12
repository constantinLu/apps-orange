package com.orange.rouber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.rouber.client.DriverProfileDto;
import data.DriverTestData;
import data.VehicleTestData;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import setup.RouberTestSetup;

import static com.orange.rouber.converter.Converter.toDriverDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class DriverControllerTest extends RouberTestSetup implements VehicleTestData, DriverTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @AfterEach
    void afterEach() {
        cleanup();
    }

    @SneakyThrows
    @Test
    void should_register_a_driver() {
        //given
        var driver = aDriver().build();
        var driverDto = toDriverDto(driver);

        // when
        var mvcResult = mockMvc.perform(post("/drivers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(status().isCreated());

        //then
        var savedDriver = get(driver);
        assertThat(savedDriver.getEmail()).isEqualTo(driverDto.getEmail());
        assertThat(savedDriver.getName()).isEqualTo(driverDto.getName());
        assertThat(savedDriver.getPhoneNumber()).isEqualTo(driverDto.getPhoneNumber());
    }


    @SneakyThrows
    @Test
    void should_throw_exception_when_request_fails_validation() {
        //given
        var driver = aDriver().email("asdsa").build();
        var driverDto = toDriverDto(driver);

        // expect
        var mvcResult = mockMvc.perform(post("/drivers")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(driverDto)))
                .andExpect(result -> assertThat(result.getResolvedException()
                        instanceof MethodArgumentNotValidException).isTrue());
    }


    @SneakyThrows
    @Test
    void should_get_driver_rating() {
        //given
        var driver = add(aDriver().id(1L).rating(5f).build());

        //when
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/drivers/1/ratings"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        assertThat(mvcResult).contains("5");
    }

    @SneakyThrows
    @Test
    void should_get_driver_info_profile() {
        //given
        var driver = add(aDriver().id(2L).rating(5f).build());
        var vehicleDto = add(anActiveVehicle(driver).build());

        //when
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/drivers/2/profile"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        var profile = objectMapper.readValue(mvcResult, DriverProfileDto.class);
        //then

        assertThat(profile.getDriverDto().getName()).isEqualTo(driver.getName());
        assertThat(profile.getVehicleDto().getVin()).isEqualTo(vehicleDto.getVin());
        assertThat(profile.getAverageTripPrice().intValue()).isEqualTo(0); //no trips yet
    }
}