package com.nhnacademy.memberservice.member.exception;

import com.nhnacademy.memberservice.common.error.BusinessException;
import com.nhnacademy.memberservice.common.error.ErrorCode;

/**
 * 주어진 이메일 주소에 해당하는 회원 정보를 찾을 수 없을 때 발생하는 예외입니다.
 * <p>
 * 이 예외는 {@link ErrorCode#MEMBER_EMAIL_NOT_FOUND}를 반환하며,
 * 주로 {@code email} 기반 회원 조회 로직에서 사용됩니다.
 * </p>
 */
public class MemberEmailNotFoundException extends BusinessException {

    /**
     * {@code MemberEmailNotFoundException}을 생성합니다.
     *
     * @param message 예외 발생 사유에 대한 설명
     */
    public MemberEmailNotFoundException(String message){
        super(message);
    }

    /**
     * 에러 코드 {@link ErrorCode#MEMBER_EMAIL_NOT_FOUND}를 반환합니다.
     *
     * @return MEMBER_EMAIL_NOT_FOUND
     */
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.MEMBER_EMAIL_NOT_FOUND;
    }
}


