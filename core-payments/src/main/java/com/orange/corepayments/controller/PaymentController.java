package com.orange.corepayments.controller;

import com.orange.corepayments.client.CorePaymentDto;
import com.orange.corepayments.client.CorePaymentResponse;
import com.orange.corepayments.client.PaymentDto;
import com.orange.corepayments.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;
import static com.orange.corepayments.client.PaymentStatusType.UNPROCESSED;
import static com.orange.corepayments.converter.Converter.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }


    @ApiOperation(value = "List of payments with statuses", notes = "Receives driver`s payments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public CorePaymentResponse readPayments(@RequestParam List<String> requestIds) {
        final var uuids = requestIds.stream()
                .map(r -> {
                    var uid = r.replace("[", "").replace("]", "");
                    return UUID.fromString(uid);
                })
                .collect(Collectors.toList());

        final var payments = paymentService.findPayments(uuids);
        return CorePaymentResponse.builder()
                .payments(toCorePaymentDtos(payments))
                .build();
    }


    @ApiOperation(value = "Authorize payment", notes = "Receives an unprocessed payment which will be AUTHORIZED")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Async
    @PostMapping
    public PaymentDto authorizePayment(@RequestBody CorePaymentDto paymentRequest) {
        Assert.isTrue(paymentRequest.getPaymentStatus().equals(UNPROCESSED), "Payment can only be UNPROCESSED");
        final var authorizePayment = paymentService.authorizePayment(toPayment(paymentRequest));
        return toPaymentDto(authorizePayment);
    }


    @ApiOperation(value = "Confirmation of payment", notes = "Receives an authorized payment which will be CONFIRMED")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @Async
    @PutMapping
    public PaymentDto confirmPayment(@RequestBody PaymentDto paymentRequest) {
        Assert.isTrue(paymentRequest.getPaymentStatus().equals(PENDING_AUTHORIZATION), "Payment can only be CONFIRMED");
        final var payment = paymentService.confirmPayment(toPayment(paymentRequest));
        return toPaymentDto(payment);
    }
}
