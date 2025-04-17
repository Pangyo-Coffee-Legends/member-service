package com.nhnacademy.memberservice.role.exception;

import com.nhnacademy.memberservice.common.error.BusinessException;
import com.nhnacademy.memberservice.common.error.ErrorCode;

/**
 * 권한(Role) 이름이 중복되어 생성 또는 수정이 불가능할 때 발생하는 예외입니다.
 * <p>
 * 주로 관리자 또는 시스템이 새로운 역할 이름을 등록하려 할 때,
 * 이미 동일한 이름의 권한이 존재하는 경우 {@link ErrorCode#ROLE_CONFLICT}와 함께 발생합니다.
 * </p>
 * <p>
 * 이 예외는 클라이언트에게 중복된 권한 이름에 대한 정보를 포함하여
 * 적절한 피드백을 전달하는 데 사용됩니다.
 * </p>
 */
public class RoleConflictException extends BusinessException {

    /**
     * 중복된 권한 이름을 포함하여 {@code RoleConflictException}을 생성합니다.
     *
     * @param roleName 중복된 권한 이름
     */
    public RoleConflictException(String roleName) {
        super("권한 이름이 중복됩니다. " + roleName);
    }

    /**
     * 에러 코드 {@link ErrorCode#ROLE_CONFLICT}를 반환합니다.
     *
     * @return ROLE_CONFLICT
     */
    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.ROLE_CONFLICT;
    }
}
