package com.nhnacademy.memberservice.common.error;

/**
 * 클라이언트에게 예외 발생 시 전달되는 에러 응답 객체이옵니다.
 * <p>
 * 에러 코드와 메시지를 포함하여, 예외의 원인을 명확하게 전달하기 위한 목적을 가집니다.
 * </p>
 */
public class ErrorResponse {
    private final String code;
    private final String message;

    /**
     * {@code ErrorResponse} 객체를 생성하옵니다.
     *
     * @param code 에러의 식별 코드
     * @param message 사용자에게 전달할 에러 메시지
     */
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() { return code; }
    public String getMessage() { return message; }
}
