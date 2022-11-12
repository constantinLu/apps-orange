package com.orange.rouber.repository;

import data.UserTestData;
import org.junit.jupiter.api.Test;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import setup.RouberTestSetup;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest extends RouberTestSetup implements UserTestData {


    @Test
    void should_by_driver_id() {
        //given
        var user = aUser().build();

        //when
        var savedUser = userRepository.save(user);


        //then
        assertThat(savedUser.getName()).isEqualTo(user.getName());
        assertThat(savedUser.getEmail()).isEqualTo(user.getEmail());
        assertThat(savedUser.getAddress()).isEqualTo(user.getAddress());
    }
}