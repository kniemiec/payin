package com.kniemiec.soft.payin;


import com.kniemiec.soft.payin.controllers.dto.*;
import com.kniemiec.soft.payin.data.PayInStatusRepository;
import com.kniemiec.soft.payin.mock.MockDataProvider;
import com.kniemiec.soft.payin.model.Status;
import com.kniemiec.soft.payin.services.Lock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class PayinIntegrationTest {

    private static final String LOCK_URI = "/lock";

    private static final String POST_URI = "/capture";

    @Autowired
    Lock lock;

    @Autowired
    WebTestClient webTestClient;

    @Autowired
    PayInStatusRepository payInStatusRepository;


    @Test
    void paymentStatusPresentWhenLockSucceed(){
        // given
        LockRequest lockRequest = MockDataProvider.prepareValidLockRequest();
        LockStatus expectedStatus = LockStatus.LOCKED;

        // when
        String lockId = webTestClient.post()
                .uri(LOCK_URI)
                .bodyValue(lockRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(LockResponse.class)
                .consumeWith( lockResponse -> {
                    var status = lockResponse.getResponseBody().getStatus();
                    assertEquals(expectedStatus, status);
                })
                .returnResult().getResponseBody().getLockId();

        var element = payInStatusRepository.findByLockId(lockId);

        StepVerifier.create(element)
                .expectNextMatches( foundElement -> foundElement.getStatus().equals(Status.LOCKED) && foundElement.getLockId().equals(lockId))
                .verifyComplete();
    }

    @Test
    void captureShouldSucceedForValidLockId(){
        // given
        LockRequest lockRequest = MockDataProvider.prepareValidLockRequest();
        LockStatus expectedStatus = LockStatus.LOCKED;

        // when
        String lockId = webTestClient.post()
                .uri(LOCK_URI)
                .bodyValue(lockRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(LockResponse.class)
                .consumeWith( lockResponse -> {
                    var status = lockResponse.getResponseBody().getStatus();
                    assertEquals(expectedStatus, status);
                })
                .returnResult().getResponseBody().getLockId();

        // then
        CaptureRequest captureRequest = MockDataProvider.prepareValidCaptureRequest(lockId);
        webTestClient.post()
                .uri(POST_URI)
                .bodyValue(captureRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CaptureResponse.class)
                .consumeWith( captureResponseEntityExchangeResult -> {
                    assertEquals(captureResponseEntityExchangeResult.getResponseBody().getStatus(), CaptureStatus.CAPTURED);
                    assertEquals(captureResponseEntityExchangeResult.getResponseBody().getLockId(), lockId);
                });
    }


    @Test
    void captureFailedForNonExistingLockId(){
        // given
        LockRequest lockRequest = MockDataProvider.prepareValidLockRequest();
        LockStatus expectedStatus = LockStatus.LOCKED;

        // when
        String lockId = webTestClient.post()
                .uri(LOCK_URI)
                .bodyValue(lockRequest)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(LockResponse.class)
                .consumeWith( lockResponse -> {
                    var status = lockResponse.getResponseBody().getStatus();
                    assertEquals(expectedStatus, status);
                })
                .returnResult().getResponseBody().getLockId();

        // then
        String newLockId = UUID.randomUUID().toString();
        CaptureRequest captureRequest = MockDataProvider.prepareValidCaptureRequest(newLockId);
        webTestClient.post()
                .uri(POST_URI)
                .bodyValue(captureRequest)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CaptureResponse.class)
                .consumeWith( captureResponseEntityExchangeResult -> {
                    assertEquals(captureResponseEntityExchangeResult.getResponseBody().getStatus(), CaptureStatus.FAILED);
                    assertNotEquals(captureResponseEntityExchangeResult.getResponseBody().getLockId(), lockId);
                });

    }
}
