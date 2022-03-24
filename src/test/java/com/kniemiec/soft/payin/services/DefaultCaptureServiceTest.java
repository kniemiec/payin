package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.CaptureStatus;
import com.kniemiec.soft.payin.data.PayInStatusRepository;
import com.kniemiec.soft.payin.mock.MockDataProvider;
import com.kniemiec.soft.payin.model.Money;
import com.kniemiec.soft.payin.model.PayInStatus;
import com.kniemiec.soft.payin.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DirtiesContext
@DataMongoTest
@ActiveProfiles("test")
class DefaultCaptureServiceTest {


    Capture captureService;

    @MockBean
    PayInStatusRepository payInStatusRepository;


    @BeforeEach
    public void setUp(){
        captureService = new DefaultCaptureService(payInStatusRepository);
    }

    @Test
    void captureWhenValidCaptureRequest() {
        // given
        String lockId = UUID.randomUUID().toString();
        CaptureRequest captureRequest = MockDataProvider.prepareValidCaptureRequest(lockId);

        PayInStatus correspondingPayInStatus = new PayInStatus(
                "transferId",
                lockId,
                new Money("PLN", BigDecimal.valueOf(100)),
                Status.CAPTURED
        );

        when(payInStatusRepository.findByLockId(any())).thenReturn(Mono.just(correspondingPayInStatus));


        // when
        var response = captureService.capture(captureRequest);

        // then
        StepVerifier.create(response)
                .expectNextMatches(captureResponse -> captureResponse.getLockId().equals(lockId) && captureResponse.getStatus().equals(CaptureStatus.CAPTURED))
                .verifyComplete();
    }

    @Test
    void captureFailedWhenInvalidLockId() {
        // given
        String lockId = UUID.randomUUID().toString();
        CaptureRequest captureRequest = MockDataProvider.prepareValidCaptureRequest(lockId);
        when(payInStatusRepository.findByLockId(any())).thenReturn(Mono.empty());

        // when
        var response = captureService.capture(captureRequest);

        // then
        StepVerifier.create(response)
                .expectNextMatches(captureResponse -> captureResponse.getLockId().equals(lockId) && captureResponse.getStatus().equals(CaptureStatus.FAILED))
                .verifyComplete();
    }

}