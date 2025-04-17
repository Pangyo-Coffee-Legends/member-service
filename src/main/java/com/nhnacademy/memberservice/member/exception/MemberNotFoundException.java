package com.nhnacademy.memberservice.member.exception;

/**
 * 회원 정보를 찾을 수 없을 때 발생하는 예외 클래스입니다.
 * <p>
 * 주로 {@code MemberService}에서 회원 번호에 해당하는 회원이 존재하지 않을 경우 발생합니다.
 * </p>
 *
 * <p>기본 메시지는 {@code "회원을 찾을 수 없습니다."}이며, 생성자에서 회원 번호를 포함한 메시지를 자동으로 구성할 수 있습니다.</p>
 */
public class MemberNotFoundException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "회원을 찾을 수 없습니다.";

    /**
     * 회원 번호를 포함하여 상세 메시지를 구성하는 예외 생성자입니다.
     *
     * @param mbNo 찾으려는 회원의 고유 식별자
     */
    public MemberNotFoundException(Long mbNo) {
        super(DEFAULT_MESSAGE + " [회원번호: " + mbNo + "]");
    }

}
