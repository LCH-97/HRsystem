package com.HelloRolha.HR.error.exception;

import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;

public class getEmployeeInSecurityContextHolderError extends BusinessException {
    public getEmployeeInSecurityContextHolderError(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
