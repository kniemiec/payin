package com.kniemiec.soft.payin.services;

import com.kniemiec.soft.payin.controllers.dto.LockRequest;
import com.kniemiec.soft.payin.controllers.dto.LockResponse;
import org.springframework.stereotype.Service;

@Service
public class DefaultLockService implements Lock{

    @Override
    public LockResponse lock(LockRequest lockRequest) {
        return null;
    }
}
