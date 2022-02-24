package com.kniemiec.soft.payin.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptureResponse {

    String captureId;
    CaptureStatus status;
}
