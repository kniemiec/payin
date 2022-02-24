package com.kniemiec.soft.payin.mock;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.model.Money;

import java.math.BigDecimal;

public class MockDataProvider {

    public static LockRequest prepareValidLockRequest(){
        return new LockRequest("senderId", new Money("PLN", BigDecimal.valueOf(100)));
    }


    public static LockRequest prepareInValidLockRequest(){
        return new LockRequest("senderId", new Money("PLN", BigDecimal.valueOf(0)));
    }
}
