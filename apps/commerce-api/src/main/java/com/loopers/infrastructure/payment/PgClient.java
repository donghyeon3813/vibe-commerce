package com.loopers.infrastructure.payment;

import com.loopers.support.config.FeignClientTimeoutConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "pgClient",
url = "localhost:8082/api/v1/payments",
configuration = FeignClientTimeoutConfig.class)
public interface PgClient {
    @PostMapping
    PaymentResponse pay(@RequestHeader("X-USER-ID") String userId,
                        @RequestBody PaymentRequest request);

}
