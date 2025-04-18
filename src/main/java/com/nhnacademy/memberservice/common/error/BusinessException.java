package com.nhnacademy.memberservice.common.error;

/**
 * {@code BusinessException}은 도메인 또는 애플리케이션 로직에서 발생하는
 * 커스텀 비즈니스 예외의 상위 클래스입니다.
 * <p>
 * 이 클래스는 런타임 예외({@link RuntimeException})를 상속하며,
 * 각 도메인 예외가 반환해야 하는 {@link ErrorCode}를 추상 메서드로 정의하여
 * 하위 예외 클래스에서 명확한 에러 코드를 지정하도록 강제합니다.
 * </p>
 * <p>
 * 예를 들어, 회원 정보를 찾을 수 없는 경우 {@code MemberNotFoundException}은
 * 이 클래스를 상속받고 {@code ErrorCode.MEMBER_NOT_FOUND}를 반환하게 됩니다.
 * </p>
 * <p>
 * 모든 커스텀 예외는 이 클래스를 상속받아 공통된 예외 처리 로직과
 * 응답 구조({@link ErrorResponse})에 활용됩니다.
 * </p>
 *
 * @author
 */
public abstract class BusinessException extends RuntimeException {

    /**
     * 지정된 메시지를 포함하는 비즈니스 예외를 생성합니다.
     *
     * @param message 예외 발생 사유에 대한 상세 메시지
     */
    public BusinessException(String message) {
        super(message);
    }

    /**
     * 예외에 해당하는 {@link ErrorCode}를 반환합니다.
     *
     * @return 이 예외에 대응되는 {@code ErrorCode}
     */
    public abstract ErrorCode getErrorCode();
}
