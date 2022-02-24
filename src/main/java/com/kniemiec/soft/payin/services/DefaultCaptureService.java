package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.CaptureResponse;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DefaultCaptureService implements Capture {

    @Override
    public Mono<CaptureResponse> capture(CaptureRequest request) {
        return null;
    }
}
