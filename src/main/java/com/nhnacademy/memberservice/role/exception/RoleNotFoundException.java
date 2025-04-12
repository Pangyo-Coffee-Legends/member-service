package com.nhnacademy.memberservice.role.exception;

/**
 * 주어진 이름의 권한(Role)을 찾을 수 없을 때 발생하는 예외입니다.
 * <p>
 * 이 예외는 주로 {@code RoleService} 또는 {@code RoleRepository}에서
 * 주어진 이름으로 권한을 조회했을 때 결과가 없을 경우 사용됩니다.
 * </p>
 */
public class RoleNotFoundException extends RuntimeException {

    /**
     * 역할 이름을 포함하여 예외 메시지를 생성합니다.
     *
     * @param roleName 찾을 수 없는 권한 이름
     */
    public RoleNotFoundException(String roleName) {
        super("해당 권한을 찾을 수 없습니다: " + roleName);
    }
}
