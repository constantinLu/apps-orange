package data;


import com.orange.rouber.model.Driver;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


public interface DriverTestData {

    LocalDateTime dateTimeNow = LocalDateTime.now();

    default Driver.DriverBuilder aDriver() {
        return Driver.builder()
                .id(new Random().nextLong())
                .phoneNumber(784312334L)
                .email("dragos.pop@email.com")
                .rating(0f)
                .trips(List.of())
                .vehicles(List.of());
    }
}
