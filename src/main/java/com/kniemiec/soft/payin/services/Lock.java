package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;

public interface Lock {

    LockResponse lock(LockRequest lockRequest);
}
