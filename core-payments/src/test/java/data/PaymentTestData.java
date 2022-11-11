package data;

import com.orange.corepayments.model.Payment;
import com.orange.corepayments.model.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;

public interface PaymentTestData {

    LocalDateTime dateTimeNow = LocalDateTime.now();

    default Payment.PaymentBuilder aPayment() {
        return Payment.builder()
                .id(new Random().nextLong())
                .amount(BigDecimal.valueOf(10.00))
                .reason("Authorize payment")
                .reward(null)
                .createdDate(dateTimeNow)
                .updatedDate(null)
                .requestId(UUID.randomUUID())
                .paymentStatus(PaymentStatus.builder()
                        .type(PENDING_AUTHORIZATION)
                        .build());
    }
}
