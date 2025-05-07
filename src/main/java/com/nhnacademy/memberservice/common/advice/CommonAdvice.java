package com.nhnacademy.memberservice.common.advice;

import com.nhnacademy.memberservice.common.error.BusinessException;
import com.nhnacademy.memberservice.common.error.ErrorCode;
import com.nhnacademy.memberservice.common.error.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * {@code CommonAdvice}는 애플리케이션 전역에서 발생하는 예외를 공통적으로 처리하는 클래스입니다.
 * <p>
 * {@code @RestControllerAdvice}를 통해 컨트롤러 계층에서 발생하는 예외를 포착하여,
 * 일관된 {@link ErrorResponse} 형식으로 클라이언트에 응답합니다.
 * </p>
 * <p>
 * {@link BusinessException}을 기반으로 한 커스텀 비즈니스 예외,
 * {@link MethodArgumentNotValidException}을 통한 유효성 검증 실패,
 * 그 외 알 수 없는 예외를 구분하여 처리합니다.
 * </p>
 * <p>
 * 주요 처리 대상은 {@code member} 도메인을 포함한 전체 모듈의 예외이며,
 * 응답 메시지 구조의 일관성과 API 오류 응답의 표준화를 위해 사용됩니다.
 * </p>
 *
 *
 */
@Slf4j
@RestControllerAdvice
public class CommonAdvice {

    /**
     * {@link BusinessException}을 처리합니다.
     * <p>
     * 도메인 서비스 내에서 발생한 비즈니스 로직 기반 예외를 포착하여
     * 사전에 정의된 {@link ErrorCode}와 함께 응답합니다.
     * </p>
     *
     * @param ex 발생한 {@code BusinessException}
     * @return {@code 400 Bad Request}와 {@link ErrorResponse} 본문
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        log.warn("비즈니스 예외 발생,{}",ex.getMessage());
        ErrorResponse error = new ErrorResponse(ex.getErrorCode(), ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * {@code @Valid} 유효성 검사 실패 시 발생하는 {@link MethodArgumentNotValidException}을 처리합니다.
     * <p>
     * 입력값 바인딩 도중 발생한 필드 오류 중 첫 번째 오류 메시지를 반환합니다.
     * </p>
     *
     * @param ex 발생한 {@code MethodArgumentNotValidException}
     * @return {@code 400 Bad Request}와 {@link ErrorResponse} 본문
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ErrorResponse error = new ErrorResponse(ErrorCode.INTERNAL_ERROR, errorMessage);
        return ResponseEntity.badRequest().body(error);
    }

    /**
     * 처리되지 않은 예외나 시스템 예외를 전역적으로 처리합니다.
     * <p>
     * 예상치 못한 오류에 대해 {@code 500 Internal Server Error} 응답과
     * 일반적인 오류 메시지를 반환합니다.
     * </p>
     *
     * @param ex 처리되지 않은 {@code Exception}
     * @return {@code 500 Internal Server Error}와 {@link ErrorResponse} 본문
     */

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse error = new ErrorResponse(ErrorCode.INTERNAL_ERROR, "서버 내부 오류가 발생하였습니다.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
