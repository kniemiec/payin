package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.CaptureRequest;
import com.kniemiec.soft.payin.controllers.dto.CaptureResponse;
import reactor.core.publisher.Mono;

public interface Capture {

    Mono<CaptureResponse> capture(CaptureRequest request);
}
