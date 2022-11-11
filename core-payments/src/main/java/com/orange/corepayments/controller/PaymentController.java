package com.orange.corepayments.controller;

import com.orange.corepayments.client.CorePaymentDto;
import com.orange.corepayments.client.CorePaymentResponse;
import com.orange.corepayments.client.PaymentDto;
import com.orange.corepayments.service.PaymentConfirmationOrchestrator;
import com.orange.corepayments.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.orange.corepayments.client.PaymentStatusType.PENDING_AUTHORIZATION;
import static com.orange.corepayments.client.PaymentStatusType.UNPROCESSED;
import static com.orange.corepayments.converter.Converter.toCorePaymentDtos;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentConfirmationOrchestrator paymentConfirmationOrchestrator;

    @Operation(summary = "List of payments with statuses. Receives driver`s payments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping()
    public CorePaymentResponse readPayments(@RequestParam List<String> requestIds) {
        final var payments = paymentService.findPayments(requestIds);
        return CorePaymentResponse.builder()
                .payments(toCorePaymentDtos(payments))
                .build();
    }


    @Operation(summary = "Authorize payment. Receives an unprocessed payment which will be AUTHORIZED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ResponseEntity authorizePayment(@RequestBody CorePaymentDto paymentRequest) {
        Assert.isTrue(paymentRequest.getPaymentStatus().equals(UNPROCESSED), "Payment can only be UNPROCESSED");
        paymentService.authorizePayment(paymentRequest);
        return ResponseEntity.accepted().build();
    }


    @Operation(summary = "Confirmation of payment. Receives an authorized payment which will be CONFIRMED")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success"),
            @ApiResponse(responseCode = "400", description = "Bad request"),
            @ApiResponse(responseCode = "401", description = "Not authenticated"),
            @ApiResponse(responseCode = "403", description = "Not authorized"),
            @ApiResponse(responseCode = "404", description = "Data not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping
    public ResponseEntity confirmPayment(@RequestBody PaymentDto paymentRequest) {
        Assert.isTrue(paymentRequest.getPaymentStatus().equals(PENDING_AUTHORIZATION), "Payment can only be CONFIRMED");
        paymentConfirmationOrchestrator.confirmPayment(paymentRequest);
        return ResponseEntity.accepted().build();
    }
}
