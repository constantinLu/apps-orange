package com.orange.corepayments.service;

import com.orange.corepayments.client.CorePaymentDto;
import com.orange.corepayments.model.Payment;
import com.orange.corepayments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static com.orange.corepayments.converter.Converter.toPayment;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientNotifierService clientNotifierService;

    public List<Payment> findPayments(List<String> requestIds) {
        return paymentRepository.findByRequestIdIn(requestIds);
    }

    public Payment find(Long id) {
        return paymentRepository.findById(id).get();
    }

    @Async
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void authorizePayment(CorePaymentDto paymentRequest) {

        var paymentEntity = toPayment(paymentRequest);

        // heavy processing here....
        sleep(1000);

        var payment = paymentRepository.save(paymentEntity.authorizePayment(paymentEntity.getAmount(), paymentEntity.getRequestId()));

        clientNotifierService.notifyTransactionComplete(payment);
    }

    @Async
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void confirmPayment(Payment incomingPayment) {

        // heavy processing here....
        sleep(1050);

        var payment = paymentRepository.save(incomingPayment.confirmPayment(incomingPayment));
        clientNotifierService.notifyTransactionComplete(payment);
    }

    private void sleep(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
