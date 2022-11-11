package com.orange.corepayments.service;

import com.orange.corepayments.client.CorePaymentDto;
import com.orange.corepayments.client.PaymentDto;
import com.orange.corepayments.model.Payment;
import com.orange.corepayments.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

import static com.orange.corepayments.converter.Converter.toPayment;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final ClientNotifierService clientNotifierService;

    public List<Payment> findPayments(List<String> requestIds) {
        return paymentRepository.findByRequestIdIn(requestIds);
    }

    @Async
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void authorizePayment(CorePaymentDto paymentRequest) {

        var paymentEntity = toPayment(paymentRequest);

        // heavy processing here....
        sleep(1000);

        var payment = paymentRepository.save(paymentEntity.authorizePayment(paymentEntity.getAmount(), paymentEntity.getRequestId()));

        clientNotifierService.notifyTransactionComplete(paymentRequest.getCallbackUrl(), payment);
    }

    @Async
    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void confirmPayment(PaymentDto paymentRequest) {

        var paymentEntity = toPayment(paymentRequest);

        // heavy processing here....
        sleep(2000);

        paymentEntity = paymentRepository.save(paymentEntity.confirmPayment(paymentEntity));
        clientNotifierService.notifyTransactionComplete(paymentRequest.getCallbackUrl(), paymentEntity);
    }

    private void sleep(int miliseconds) {
        try {
            Thread.sleep(miliseconds);
        } catch (InterruptedException e) {

            throw new RuntimeException(e);
        }
    }

}
