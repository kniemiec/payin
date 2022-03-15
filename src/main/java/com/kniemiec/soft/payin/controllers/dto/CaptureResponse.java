package com.kniemiec.soft.payin.controllers.dto;

import com.kniemiec.soft.payin.model.PayInStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptureResponse {

    String lockId;
    CaptureStatus status;

    public static CaptureResponse from(PayInStatus payInStatus){
        return new CaptureResponse(payInStatus.getLockId(),
                CaptureStatus.CAPTURED);
    }
}
