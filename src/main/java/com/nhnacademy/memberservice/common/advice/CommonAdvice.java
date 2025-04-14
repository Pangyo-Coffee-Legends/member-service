package com.nhnacademy.memberservice.common.advice;

import com.nhnacademy.memberservice.common.error.ErrorResponse;
import com.nhnacademy.memberservice.member.exception.MemberNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@code CommonAdvice}는 애플리케이션 전역에서 발생하는 예외를 공통으로 처리하는 클래스이옵니다.
 * <p>
 * {@code @RestControllerAdvice}를 활용하여, Controller 계층에서 발생하는 예외를 포착하고
 * 일관된 {@code ErrorResponse} 형식으로 클라이언트에 응답을 반환하옵니다.
 * </p>
 * <p>
 * 주로 {@code member} 도메인과 관련된 예외 처리를 담당하오며, 유효성 검증 실패나 시스템 예외 등도 함께 처리하옵니다.
 * </p>
 *
 * @author
 */
@Slf4j
@RestControllerAdvice
public class CommonAdvice {

    /**
     * 회원 정보를 찾을 수 없을 경우 발생하는 {@link MemberNotFoundException} 예외를 처리합니다.
     *
     * @param ex 발생한 {@code MemberNotFoundException}
     * @return 404 Not Found 상태 코드와 함께 에러 응답 반환
     */
    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFound(MemberNotFoundException ex) {
        ErrorResponse error = new ErrorResponse("MEMBER_NOT_FOUND", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * {@code @Valid} 유효성 검사 실패 시 발생하는 {@link MethodArgumentNotValidException}을 처리합니다.
     *
     * @param ex 발생한 {@code MethodArgumentNotValidException}
     * @return 400 Bad Request 상태 코드와 함께 첫 번째 오류 메시지를 담은 에러 응답 반환
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ErrorResponse error = new ErrorResponse("INVALID_REQUEST", errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 처리되지 않은 일반적인 예외들을 처리합니다.
     *
     * @param ex 발생한 {@code Exception}
     * @return 500 Internal Server Error 상태 코드와 함께 일반적인 에러 메시지를 담은 응답 반환
     */
    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse("INTERNAL_ERROR", "서버 내부 오류가 발생하였습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
