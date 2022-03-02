package com.kniemiec.soft.payin.model;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayInStatus {

    String id;
    String lockId;
    Money money;
    Status status;


    public static PayInStatus from(LockRequest lockRequest, String lockId){
        return new PayInStatus(
            lockRequest.getTransferId(),
            lockId,
            lockRequest.getMoney(),
            Status.LOCKED
        );
    }
}
