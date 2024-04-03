package com.HelloRolha.HR.error.exception;

import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;

public class OvertimeSQLException extends BusinessException {

    public OvertimeSQLException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }
}
