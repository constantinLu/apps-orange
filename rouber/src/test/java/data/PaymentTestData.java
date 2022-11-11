package data;


import com.orange.rouber.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;


public interface PaymentTestData {

    LocalDateTime dateTimeNow = LocalDateTime.now();

    default Payment.PaymentBuilder aPayment() {
        return Payment.builder()
                .id(new Random().nextLong())
                .paidPrice(BigDecimal.valueOf(10.00))
                .startInitiation(dateTimeNow)
                .endConfirmation(null)
                .requestId(UUID.randomUUID().toString());
    }
}
