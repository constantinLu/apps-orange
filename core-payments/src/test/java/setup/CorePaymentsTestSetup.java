package setup;

import com.orange.corepayments.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class CorePaymentsTestSetup {

    @Autowired
    protected PaymentRepository paymentRepository;


}
