package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.CaptureResponse;
import com.kniemiec.soft.payin.mock.MockDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

import java.util.UUID;

class DefaultCaptureServiceTest {


    Capture captureService;

    @BeforeEach
    public void setUp(){
        captureService = new DefaultCaptureService();
    }

    @Test
    void captureWhenValidCaptureRequest() {
        // given
        String lockId = UUID.randomUUID().toString();
        CaptureRequest captureRequest = MockDataProvider.prepareValidCaptureRequest(lockId);


        // when
        var response = captureService.capture(captureRequest);

        // then
        StepVerifier.create(response)
                .expectNextMatches(captureResponse -> captureResponse.getCaptureId().equals(lockId))
                .verifyComplete();

    }
}