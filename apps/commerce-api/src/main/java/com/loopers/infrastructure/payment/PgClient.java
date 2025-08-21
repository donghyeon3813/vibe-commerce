package com.loopers.infrastructure.payment;

import com.loopers.support.config.FeignClientTimeoutConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "pgClient",
url = "localhost:8082/api/v1/payments",
configuration = FeignClientTimeoutConfig.class)
public interface PgClient {
    @PostMapping
    PaymentResponse pay(@RequestHeader("X-USER-ID") String userId,
                        @RequestBody PaymentRequest request);

    @GetMapping("/{transactionKey}")
    PaymentResponse get(
            @PathVariable("transactionKey") String transactionKey,
            @RequestHeader("X-USER-ID") String userId
    );

}
