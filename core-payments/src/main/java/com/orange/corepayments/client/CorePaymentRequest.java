package com.orange.corepayments.client;

import lombok.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class CorePaymentRequest {

    private List<UUID> requestIds;
}
