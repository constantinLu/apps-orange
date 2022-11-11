package com.orange.rouber.client.corepayments;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CorePaymentResponse {

    private List<CorePaymentDto> payments;
}
