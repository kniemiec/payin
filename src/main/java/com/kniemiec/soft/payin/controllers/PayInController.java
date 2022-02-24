package com.kniemiec.soft.payin.controllers;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.controllers.dto.LockStatus;
import com.kniemiec.soft.payin.model.PayInStatus;
import com.kniemiec.soft.payin.model.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.util.UUID;


@RestController
public class PayInController {

    private static Logger logger = LoggerFactory.getLogger(PayInController.class);

    private Sinks.Many<PayInStatus> payInStatuses = Sinks.many().replay().all();

    @PostMapping("/lock")
    public Mono<LockResponse> lock(@RequestBody LockRequest lockRequest){
        logger.info("Request received: {}",lockRequest);
        String lockId = UUID.randomUUID().toString();
        if(lockRequest.getMoney().getValue().signum() > 0) {
            return Mono.just(new LockResponse( lockId,  LockStatus.LOCKED))
//                    .doOnNext( status -> payInStatuses.tryEmitNext(status))
                    .log();
        } else {
            return Mono.just(new LockResponse( lockId,  LockStatus.LOCK_FAILED))
//                    .doOnNext( status -> payInStatuses.tryEmitNext(status))
                    .log();
        }
    }

//    @PostMapping("/capture")
//    public Mono<PayInStatus> capture(@RequestBody CaptureRequest captureRequest){
//
//    }

    @GetMapping(value = "/statuses",  produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PayInStatus> statues(){
        return payInStatuses.asFlux().log();
    }
}
