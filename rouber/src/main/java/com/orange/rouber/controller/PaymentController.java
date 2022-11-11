package com.orange.rouber.controller;

import com.orange.rouber.client.corepayments.CorePaymentDto;
import com.orange.rouber.service.PaymentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @ApiOperation(value = "GET List of payments", notes = "Payment info and statuses for each driver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 400, message = "Bad request"),
            @ApiResponse(code = 401, message = "Not authenticated"),
            @ApiResponse(code = 403, message = "Not authorized"),
            @ApiResponse(code = 404, message = "Data not found"),
            @ApiResponse(code = 500, message = "Internal server error")
    })
    @GetMapping("/driver/{driverId}")
    List<CorePaymentDto> readDriverPayments(@PathVariable Long driverId) {
        return paymentService.readDriverPayments(driverId);
    }
}
