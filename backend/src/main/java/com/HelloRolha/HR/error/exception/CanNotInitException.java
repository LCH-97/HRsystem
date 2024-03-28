package com.HelloRolha.HR.error.exception;

import com.HelloRolha.HR.error.BusinessException;
import com.HelloRolha.HR.error.ErrorCode;

public class CanNotInitException extends BusinessException {
    public CanNotInitException() {
        super(ErrorCode.INIT_FAIL, " 관리자가 존재합니다. ");
    }

}
