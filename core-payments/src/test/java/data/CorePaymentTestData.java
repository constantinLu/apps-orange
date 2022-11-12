package data;

import com.orange.corepayments.client.CorePaymentDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;

public interface CorePaymentTestData {

    LocalDateTime dateTimeNow = LocalDateTime.now();

    default CorePaymentDto.CorePaymentDtoBuilder aCorePaymentDto() {
        return CorePaymentDto.builder()
                .amount(BigDecimal.valueOf(10.00))
                .reason(Optional.of("Authorize payment"))
                .reward(null)
                .createdDate(dateTimeNow)
                .updatedDate(null)
                .requestId(UUID.randomUUID().toString())
                .paymentStatus(PENDING_AUTHORIZATION);

    }
}
