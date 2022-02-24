package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.controllers.dto.LockStatus;
import com.kniemiec.soft.payin.mock.MockDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


class DefaultLockServiceTest {

    Lock lockService;

    @BeforeEach
    public void setUp(){
        lockService = new DefaultLockService();
    }

    @Test
    public void testLock_whenValidRequest(){
        LockRequest validRequest = MockDataProvider.prepareValidLockRequest();
        Mono<LockResponse> response = lockService.lock(validRequest);

        StepVerifier.create(response)
                .expectNextMatches( lockResponse -> lockResponse.getStatus().equals(LockStatus.LOCKED))
                .verifyComplete();
    }

    @Test
    public void testLock_whenInValidRequest(){
        LockRequest validRequest = MockDataProvider.prepareInValidLockRequest();
        Mono<LockResponse> response = lockService.lock(validRequest);

        StepVerifier.create(response)
                .expectNextMatches( lockResponse -> lockResponse.getStatus().equals(LockStatus.LOCKED))
                .verifyComplete();
    }
}