package data;

import com.orange.rouber.model.Driver;
import com.orange.rouber.model.Trip;
import com.orange.rouber.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public interface TripTestData extends PointTestData, PaymentTestData {


    default Trip.TripBuilder aFinalizedTrip(User requestedBy, Driver assignedTo) {
        return anOngoingTrip(requestedBy, assignedTo)
                .endTrip(LocalDateTime.now())
                .payment(aPayment().build())
                .rating(4f);
    }
    default Trip.TripBuilder anOngoingTrip(User requestedBy, Driver assignedTo) {
        return aTrip(requestedBy)
                .startTrip(LocalDateTime.now().minus(2, SECONDS))
                .assignedTo(assignedTo)
                .endTrip(null);
    }

    default Trip.TripBuilder aTrip(User requestedBy) {
        return Trip.builder()
                .id(random.nextLong())
                .price(BigDecimal.TEN)
                .startLocation(aPoint().build())
                .endLocation(aPoint().build())
                .rating(null)
                .requestedBy(requestedBy)
                .assignedTo(null)
                .startTrip(null)
                .endTrip(null)
                .payment(null);
    }
}
