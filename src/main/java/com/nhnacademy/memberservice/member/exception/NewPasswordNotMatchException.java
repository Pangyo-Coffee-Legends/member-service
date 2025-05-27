package com.nhnacademy.memberservice.member.exception;

import com.nhnacademy.memberservice.common.error.BusinessException;
import com.nhnacademy.memberservice.common.error.ErrorCode;

/**
 * 새로운 비밀번호와 비밀번호 확인 값이 일치하지 않을 경우 발생하는 예외입니다.
 * <p>
 * 비밀번호 변경 요청 시 두 필드의 값이 불일치할 경우 이 예외가 throw되며,
 * {@link ErrorCode#NEW_PASSWORD_NOT_MATCH}를 반환합니다.
 * </p>
 */
public class NewPasswordNotMatchException extends BusinessException {

    /**
     * 기본 메시지를 포함한 {@code NewPasswordNotMatchException} 생성자입니다.
     */
    public NewPasswordNotMatchException() {
        super("새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }

    /**
     * 에러 코드 {@link ErrorCode#NEW_PASSWORD_NOT_MATCH}를 반환합니다.
     *
     * @return NEW_PASSWORD_NOT_MATCH
     */
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.NEW_PASSWORD_NOT_MATCH;
    }
}


