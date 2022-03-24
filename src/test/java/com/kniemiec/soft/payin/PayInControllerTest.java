package com.kniemiec.soft.payin;


import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.controllers.dto.LockStatus;
import com.kniemiec.soft.payin.model.Money;
import com.kniemiec.soft.payin.services.Lock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

//@WebFluxTest( controllers = PayInController.class)
@DirtiesContext
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
public class PayInControllerTest {


    @Autowired
    WebTestClient webTestClient;

    @MockBean
    Lock lockService;

    @Test
    void shouldReturnStatusLockedWhenValidAmount(){
        // given
        LockRequest lockRequest = new LockRequest("senderId", new Money("PLN", BigDecimal.valueOf(100)));
        LockResponse lockResponse = new LockResponse(UUID.randomUUID().toString(), LockStatus.LOCKED);

        when(lockService.lock(any())).thenReturn(Mono.just(lockResponse));

        // when
        // then
         webTestClient.post()
                .uri("/lock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(lockRequest),LockRequest.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(LockResponse.class)
                .consumeWith(
                        response -> assertEquals(response.getResponseBody().blockFirst().getStatus(), LockStatus.LOCKED)
                );
    }


    @Test
    void shouldReturnBadWhenInvalidMoney(){
        // given
        LockRequest invalidLockRequest = new LockRequest("senderId", new Money("PLN", BigDecimal.valueOf(0)));
        LockResponse lockResponse = new LockResponse(UUID.randomUUID().toString(), LockStatus.LOCK_FAILED);

        when(lockService.lock(any())).thenReturn(Mono.just(lockResponse));

        // when
        // then


        webTestClient.post()
                .uri("/lock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(invalidLockRequest),LockRequest.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(LockResponse.class)
                .consumeWith(
                        response -> assertEquals(response.getResponseBody().blockFirst().getStatus(), LockStatus.LOCK_FAILED)
                );
    }
}
