package com.kniemiec.soft.payin.controllers.dto;


import com.kniemiec.soft.payin.model.Money;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LockRequest {

    String senderId;

    Money money;
}
