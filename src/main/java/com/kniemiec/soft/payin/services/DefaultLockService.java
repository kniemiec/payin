package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import com.kniemiec.soft.payin.controllers.dto.LockStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DefaultLockService implements Lock {

    @Override
    public Mono<LockResponse> lock(LockRequest lockRequest) {
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
}
