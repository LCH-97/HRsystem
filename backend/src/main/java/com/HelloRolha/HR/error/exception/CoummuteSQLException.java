package com.HelloRolha.HR.error.exception;

import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;

public class CoummuteSQLException extends BusinessException {
    public CoummuteSQLException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
