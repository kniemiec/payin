package com.kniemiec.soft.payin;


import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.controllers.dto.LockStatus;
import com.kniemiec.soft.payin.model.Money;
import com.kniemiec.soft.payin.controllers.PayInController;
import com.kniemiec.soft.payin.services.Lock;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

//@WebFluxTest( controllers = PayInController.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class PayInControllerTest {


    @Autowired
    WebTestClient webTestClient;

    @Test
    void shouldReturnStatusLockedWhenValidAmount(){
         webTestClient.post()
                .uri("/lock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LockRequest("senderId", new Money("PLN", BigDecimal.valueOf(100)))),LockRequest.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(LockResponse.class)
                .consumeWith(
                        response -> {
                            assertEquals(response.getResponseBody().blockFirst().getStatus(), LockStatus.LOCKED);
                        }
                );
    }


    @Test
    void shouldReturnBadWhenInvalidMoney(){
        webTestClient.post()
                .uri("/lock")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(new LockRequest("senderId", new Money("PLN", BigDecimal.valueOf(0)))),LockRequest.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .returnResult(LockResponse.class)
                .consumeWith(
                        response -> {
                            assertEquals(response.getResponseBody().blockFirst().getStatus(), LockStatus.LOCK_FAILED);
                        }
                );
    }
}
