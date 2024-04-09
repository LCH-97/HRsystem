package com.HelloRolha.HR.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    // 공통
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-001", "유효성 검증에 실패한 경우"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-002", "서버에서 처리할 수 없는 경우"),
    CONSTRAINT_VIOLATION(HttpStatus.BAD_REQUEST, "COMMON-003", "테이블 컬럼의 제약조건을 위반 한 경우"),

    // 계정
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "ACCOUNT-001", "인증에 실패한 경우"),
    TOKEN_NOT_EXISTS(HttpStatus.UNAUTHORIZED, "ACCOUNT-002", "토큰이 존재하지 않는 경우"),
    INVALID_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED, "ACCOUNT-003", "토큰이 유효하지 않은 경우"),
    EXPIRED_VERIFICATION_TOKEN(HttpStatus.UNAUTHORIZED, "ACCOUNT-004", "토큰의 유효기간이 만료된 경우"),


    // 회원
    DUPLICATE_SIGNUP_EMAIL(HttpStatus.BAD_REQUEST, "USER-001", "회원 이메일이 중복된 경우"),
    DUPLICATE_SIGNUP_PHONENUMBER(HttpStatus.BAD_REQUEST, "USER-002", "회원 전화번호가 중복된 경우"),
    USER_NOT_EXISTS(HttpStatus.NOT_FOUND, "USER-003", "회원을 찾을 수 없는 경우"),
    DIFFERENT_USER_PASSWORD(HttpStatus.BAD_REQUEST, "USER-004", "회원의 패스워드가 저장된 데이터와 다른 경우"),


    AlreadyCommute(HttpStatus.BAD_REQUEST, "COMMUTE-001", "한 사람이 같은 날짜에 출근할 경우"),
    USER_UNCERTIFIED(HttpStatus.UNAUTHORIZED, "EMPLOYEE-001" , "로그인 시도 시, 인증 받은 사용자가 아닌 경우"),

    INIT_FAIL(HttpStatus.BAD_REQUEST,"INIT_FAIL","관리자가 이미 존재합니다."),

    DB_NULL(HttpStatus.INTERNAL_SERVER_ERROR,"DB-NULL","DB가 비어있습니다."),
    DB_ERROR_SQL(HttpStatus.INTERNAL_SERVER_ERROR,"DB-ERROR","SQL문 실행이 실패했습니다."),
    DB_ERROR_CREATE_SQL(HttpStatus.INTERNAL_SERVER_ERROR, "DB-ERROR-CREATE" ,"CREATE 실행이 실패했습니다." ),
    EMPLOYEE_GET_FAIL_IN_SECURITYCONTEXTHOLDER(HttpStatus.INTERNAL_SERVER_ERROR, "EMPLOYEE-002", "인가된 유저 정보를 가져오는 것에 실패하였습니다.");

    private final HttpStatus status;  // 헤더로 반환할 Http 상태 코드
    private final String code;    // 페이로드로 반환할 에러 코드
    private final String description;  // 에러 코드 문서화를 위한 설명

    ErrorCode(HttpStatus status, String code, String description) {
        this.status = status;
        this.code = code;
        this.description = description;
    }

    public HttpStatus getStatus() { return status; }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
