package setup;

import com.orange.rouber.model.*;
import com.orange.rouber.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Random;

import static com.orange.rouber.model.Vehicle.State.ACTIVE;

public class RouberTestSetup {

    protected final static Random RANDOM = new Random();

    @Autowired
    protected PaymentRepository paymentRepository;

    @Autowired
    protected DriverRepository driverRepository;

    @Autowired
    protected VehicleRepository vehicleRepository;

    @Autowired
    protected TripRepository tripRepository;

    @Autowired
    protected UserRepository userRepository;


    protected Payment add(Payment payment) {
        return paymentRepository.save(payment);
    }

    protected User add(User user) {
        return userRepository.save(user);
    }

    protected Driver add(Driver user) {
        return driverRepository.save(user);
    }

    protected Trip add(Trip trip) {
        return tripRepository.save(trip);
    }

    protected Vehicle add(Vehicle vehicle) {
        return vehicleRepository.save(vehicle);
    }

    protected User get(User user) {
        return userRepository.getReferenceById(user.getId());
    }

    protected Trip get(Trip trip) {
        return tripRepository.findByRequestedBy_Id(trip.getRequestedBy().getId());
    }

    protected Driver get(Driver driver) {
        return driverRepository.findByEmail(driver.getEmail());
    }

    protected Payment get(String requestId) {
        return paymentRepository.findByRequestId(requestId);
    }

    protected List<Payment> getAll(List<String> requestId) {
        return paymentRepository.findByRequestIdIn(requestId);
    }

    protected List<Vehicle> getByDriver(Long driverId) {
        return vehicleRepository.findByDriver_IdAndState(driverId, ACTIVE);
    }

    protected void cleanup() {
        paymentRepository.deleteAll();
        tripRepository.deleteAll();
        driverRepository.deleteAll();
        vehicleRepository.deleteAll();
        userRepository.deleteAll();

    }
}
