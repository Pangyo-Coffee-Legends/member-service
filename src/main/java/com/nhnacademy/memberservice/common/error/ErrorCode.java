package com.nhnacademy.memberservice.common.error;

/**
 * {@code ErrorCode}는 애플리케이션에서 발생할 수 있는 주요 예외 상황을
 * 식별하기 위한 열거형(enum)입니다.
 * <p>
 * 각 예외 코드 값은 클라이언트에게 전달되는 {@link com.nhnacademy.memberservice.common.error.ErrorResponse}
 * 객체에 포함되어, 오류의 유형을 명확히 구분하는 데 사용됩니다.
 * </p>
 * <p>
 * 클라이언트는 이 코드 값을 기준으로 UI 메시지 분기 처리나
 * 예외 상황에 대한 별도 대응이 가능합니다.
 * </p>
 */
public enum ErrorCode {

    /**
     * 회원 정보가 존재하지 않을 때 발생합니다.
     */
    MEMBER_NOT_FOUND,

    /**
     * 특정 이메일에 해당하는 회원을 찾을 수 없을 때 발생합니다.
     */
    MEMBER_EMAIL_NOT_FOUND,

    /**
     * 요청한 역할(Role) 정보가 존재하지 않을 때 발생합니다.
     */
    ROLE_NOT_FOUND,

    /**
     * 시스템 내부에서 처리되지 않은 예외가 발생한 경우 사용됩니다.
     */
    INTERNAL_ERROR,

    /**
     * 역할(Role) 정보가 중복되었거나 충돌이 발생한 경우 사용됩니다.
     */
    ROLE_CONFLICT,

    /**
     * 회원 정보가 중복되었거나 충돌이 발생한 경우 사용됩니다.
     */
    MEMBER_CONFLICT,

    /**
     * 입력한 비밀번호가 기존 비밀번호와 일치하지 않을 경우 사용됩니다.
     */
    PASSWORD_NOT_MATCH,

    /**
     * 새 비밀번호와 비밀번호 확인 값이 일치하지 않을 경우 사용됩니다.
     */
    NEW_PASSWORD_NOT_MATCH,

    UNKNOWN_ERROR
}
