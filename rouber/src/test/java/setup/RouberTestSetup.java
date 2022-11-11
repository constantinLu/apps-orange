package setup;

import com.orange.rouber.repository.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Random;

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


}
