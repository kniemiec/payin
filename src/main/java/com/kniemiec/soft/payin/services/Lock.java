package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import reactor.core.publisher.Mono;

public interface Lock {
    Mono<LockResponse> lock(LockRequest lockRequest);
}
