package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.CaptureResponse;
import com.kniemiec.soft.payin.controllers.dto.CaptureStatus;
import com.kniemiec.soft.payin.data.PayInStatusRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DefaultCaptureService implements Capture {

    PayInStatusRepository payInStatusRepository;

    public DefaultCaptureService(PayInStatusRepository payInStatusRepository){
        this.payInStatusRepository = payInStatusRepository;
    }

    @Override
    public Mono<CaptureResponse> capture(CaptureRequest request) {
        return payInStatusRepository.findByLockId(request.getLockId())
                .map( payInStatus -> CaptureResponse.from(payInStatus))
                .switchIfEmpty( Mono.just(new CaptureResponse(request.getLockId(), CaptureStatus.FAILED)));
    }
}
