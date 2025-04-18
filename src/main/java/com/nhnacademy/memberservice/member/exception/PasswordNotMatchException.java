package com.nhnacademy.memberservice.member.exception;

import com.nhnacademy.memberservice.common.error.BusinessException;
import com.nhnacademy.memberservice.common.error.ErrorCode;

/**
 * 기존 비밀번호와 입력된 비밀번호가 일치하지 않을 경우 발생하는 예외입니다.
 * <p>
 * 비밀번호 검증 실패 시 사용되며,
 * {@link ErrorCode#PASSWORD_NOT_MATCH}를 반환합니다.
 * </p>
 */
public class PasswordNotMatchException extends BusinessException {

    /**
     * 기본 메시지를 포함한 {@code PasswordNotMatchException} 생성자입니다.
     */
    public PasswordNotMatchException() {
        super("기존 비밀번호가 일치하지 않습니다.");
    }

    /**
     * 에러 코드 {@link ErrorCode#PASSWORD_NOT_MATCH}를 반환합니다.
     *
     * @return PASSWORD_NOT_MATCH
     */
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.PASSWORD_NOT_MATCH;
    }
}

