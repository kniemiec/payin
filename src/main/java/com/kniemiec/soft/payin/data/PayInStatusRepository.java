package com.kniemiec.soft.payin.data;

import com.kniemiec.soft.payin.model.PayInStatus;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface PayInStatusRepository extends ReactiveMongoRepository<PayInStatus, String> {

    Mono<PayInStatus> findByLockId(String lockId);

}
