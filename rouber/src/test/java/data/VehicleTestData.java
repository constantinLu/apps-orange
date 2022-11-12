package data;


import com.orange.rouber.model.Driver;
import com.orange.rouber.model.Vehicle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Random;

import static com.orange.rouber.model.Vehicle.State.ACTIVE;
import static com.orange.rouber.model.Vehicle.State.INACTIVE;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.time.temporal.ChronoUnit.YEARS;


public interface VehicleTestData {

    LocalDateTime dateTimeNow = LocalDateTime.now();

    default Vehicle.VehicleBuilder anActiveVehicle(Driver driver) {
        return aVehicle()
                .driver(driver)
                .state(ACTIVE);
    }

    default Vehicle.VehicleBuilder aVehicle() {
        return Vehicle.builder()
                .id(new Random().nextLong())
                .color("Red")
                .brand("Bmw")
                .driver(null)
                .createdDate(dateTimeNow.minus(1, SECONDS))
                .registerDate(LocalDate.now().minus(1, YEARS))
                .vin("WAERASDF123GWC")
                .licensePlate("B43STS")
                .state(INACTIVE)
                .name("E22");
    }
}
