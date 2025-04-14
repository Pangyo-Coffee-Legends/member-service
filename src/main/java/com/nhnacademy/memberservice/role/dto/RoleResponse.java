package com.nhnacademy.memberservice.role.dto;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 권한(Role) 정보 응답을 위한 DTO 클래스입니다.
 * <p>
 * 이 클래스는 클라이언트에게 권한의 정보를 전달할 때 사용되며,
 * 권한의 이름({@code roleName})과 설명({@code roleDescription})을 포함합니다.
 * 예를 들어, "ADMIN", "일반 사용자 권한" 등의 정보를 응답합니다.
 * </p>
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoleResponse {

    private String roleName;

    private String roleDescription;

    public RoleResponse(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }
}
