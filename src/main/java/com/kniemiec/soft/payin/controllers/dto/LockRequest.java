package com.kniemiec.soft.payin.controllers.dto;


import com.kniemiec.soft.payin.model.Money;
import com.kniemiec.soft.payin.model.PayInStatus;
import com.kniemiec.soft.payin.model.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockRequest {

    String transferId;
    Money money;

    public PayInStatus toPayInStatus(String lockId){
        return new PayInStatus(
            this.getTransferId(),
            lockId,
            this.money,
                Status.LOCKED
        );
    }
}
