package com.kniemiec.soft.payin.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PayInStatus {

    String id;
    String lockId;
    Money money;
    Status status;
}
