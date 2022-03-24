package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.controllers.dto.LockStatus;
import com.kniemiec.soft.payin.data.PayInStatusRepository;
import com.kniemiec.soft.payin.mock.MockDataProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DirtiesContext
@DataMongoTest
@ActiveProfiles("test")
class DefaultLockServiceTest {

    Lock lockService;

    @MockBean
    PayInStatusRepository payInStatusRepository;

    @BeforeEach
    public void setUp(){
        lockService = new DefaultLockService(payInStatusRepository);
    }

    @Test
    public void testLock_whenValidRequest(){
        // given
        LockRequest validRequest = MockDataProvider.prepareValidLockRequest();
        String mockLockId = UUID.randomUUID().toString();
        when(payInStatusRepository.save(any())).thenReturn(Mono.just(validRequest.toPayInStatus(mockLockId)));

        // when
        Mono<LockResponse> response = lockService.lock(validRequest);

        // then
        StepVerifier.create(response)
                .expectNextMatches( lockResponse -> lockResponse.getStatus().equals(LockStatus.LOCKED))
                .verifyComplete();

        verify(payInStatusRepository).save(any());
    }

    @Test
    public void testLock_whenInValidRequest(){
        LockRequest validRequest = MockDataProvider.prepareInValidLockRequest();
        Mono<LockResponse> response = lockService.lock(validRequest);

        StepVerifier.create(response)
                .expectNextMatches( lockResponse -> lockResponse.getStatus().equals(LockStatus.LOCK_FAILED))
                .verifyComplete();

        verify(payInStatusRepository, times(0)).save(any());
    }
}