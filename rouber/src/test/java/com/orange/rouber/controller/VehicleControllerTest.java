package com.orange.rouber.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orange.rouber.client.VehicleDto;
import data.DriverTestData;
import data.VehicleTestData;
import lombok.SneakyThrows;
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

import java.util.List;

import static com.orange.rouber.converter.Converter.toVehicleDto;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class VehicleControllerTest extends RouberTestSetup implements VehicleTestData, DriverTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;


    @SneakyThrows
    @Test
    void should_register_a_vehicle() {
        //given
        var driver = add(aDriver().build());
        var vehicleDto = toVehicleDto(aVehicle().driver(driver).build());

        // when
        var mvcResult = mockMvc.perform(post("/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDto)))
                .andExpect(status().isCreated());

        var savedVehicle = getByDriver(driver.getId()).get(0);
        assertThat(savedVehicle.getBrand()).isEqualTo(vehicleDto.getBrand());
        assertThat(savedVehicle.getVin()).isEqualTo(vehicleDto.getVin());
        assertThat(savedVehicle.getRegisterDate()).isEqualTo(vehicleDto.getRegisterDate());
        assertThat(savedVehicle.getName()).isEqualTo(vehicleDto.getName());
    }

    @SneakyThrows
    @Test
    void should_throw_exception_when_payment_is_not_unprocessed() {
        //given
        var vehicleDto = VehicleDto.builder().name("Dacia").build();

        // expect
        var mvcResult = mockMvc.perform(post("/vehicles")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vehicleDto)))
                .andExpect(result -> assertThat(result.getResolvedException()
                        instanceof MethodArgumentNotValidException).isTrue());
    }

    @SneakyThrows
    @Test
    void should_get_history_of_vehicles() {
        //given
        var driver = add(aDriver().build());
        var vehicleDto1 = add(aVehicle().driver(driver).build());
        var vehicleDto2 = add(aVehicle().driver(driver).build());

        //when
        var mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/vehicles/history"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then
        List<VehicleDto> vehicleHistories = objectMapper.readerForListOf(VehicleDto.class).readValue(mvcResult);
        assertThat(vehicleHistories.size() == 2).isTrue();
    }

}