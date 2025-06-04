package com.nhnacademy.memberservice.common.error;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * 클라이언트에게 예외 발생 시 전달되는 에러 응답 객체입니다.
 * <p>
 * 에러 코드({@link ErrorCode})와 사용자 메시지, 그리고 발생 시각 정보를 포함하며,
 * 예외 상황에 대한 정보를 구조화하여 클라이언트에 전달하기 위한 용도로 사용됩니다.
 * </p>
 */
@Slf4j
@Getter
public class ErrorResponse {
    private final ErrorCode code;
    private final String message;
    private final LocalDateTime timestamp;

    /**
     * {@code ErrorResponse} 객체를 생성합니다.
     *
     * @param code    에러의 식별 코드
     * @param message 사용자에게 전달할 에러 메시지
     */
    public ErrorResponse(ErrorCode code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();

        log.debug("code : {}, message : {}, timestamp : {}", this.code, this.message, this.timestamp);
    }
}
