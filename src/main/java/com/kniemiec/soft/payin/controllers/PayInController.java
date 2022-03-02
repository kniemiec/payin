package com.kniemiec.soft.payin.controllers;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.CaptureResponse;
import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.model.PayInStatus;
import com.kniemiec.soft.payin.services.Capture;
import com.kniemiec.soft.payin.services.Lock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;


@RestController
public class PayInController {

    private static Logger logger = LoggerFactory.getLogger(PayInController.class);

    private Sinks.Many<PayInStatus> payInStatuses = Sinks.many().replay().all();

    private Lock lockService;

    private Capture captureService;

    public PayInController(Lock lockService, Capture captureService){
        this.lockService = lockService;
        this.captureService = captureService;
    }

    @PostMapping("/lock")
    public ResponseEntity<Mono<LockResponse>> lock(@RequestBody LockRequest lockRequest){
        logger.info("Request received: {}",lockRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(lockService.lock(lockRequest));
    }

    @PostMapping("/capture")
    public Mono<CaptureResponse> capture(@RequestBody CaptureRequest captureRequest){
        return captureService.capture(captureRequest);
    }

    @GetMapping(value = "/statuses",  produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PayInStatus> statues(){
        return payInStatuses.asFlux().log();
    }
}
