package com.nhnacademy.memberservice.member.exception;

import com.nhnacademy.memberservice.common.error.BusinessException;
import com.nhnacademy.memberservice.common.error.ErrorCode;

/**
 * 회원 번호에 해당하는 회원 정보가 존재하지 않을 때 발생하는 예외입니다.
 * <p>
 * 주로 {@code MemberService}에서 회원 고유 번호({@code mbNo}) 기반 조회 시
 * 데이터가 존재하지 않을 경우 사용됩니다.
 * </p>
 * <p>
 * 에러 응답에는 {@link ErrorCode#MEMBER_NOT_FOUND}가 포함됩니다.
 * </p>
 */
public class MemberNotFoundException extends BusinessException {

    private static final String DEFAULT_MESSAGE = "회원을 찾을 수 없습니다.";

    /**
     * 회원 번호를 포함한 상세 메시지와 함께 예외를 생성합니다.
     *
     * @param mbNo 찾으려는 회원의 고유 식별자
     */
    public MemberNotFoundException(Long mbNo) {
        super(DEFAULT_MESSAGE + " [회원번호: " + mbNo + "]");
    }

    /**
     * 에러 코드 {@link ErrorCode#MEMBER_NOT_FOUND}를 반환합니다.
     *
     * @return MEMBER_NOT_FOUND
     */
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.MEMBER_NOT_FOUND;
    }
}


