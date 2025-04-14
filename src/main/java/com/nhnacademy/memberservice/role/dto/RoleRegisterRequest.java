package com.nhnacademy.memberservice.role.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;


/**
 * 권한(Role) 등록 요청을 위한 DTO 클래스입니다.
 * <p>
 * 이 클래스는 관리자가 새로운 사용자 권한을 정의할 때 사용됩니다.
 * 예: USER, ADMIN 등.
 * </p>
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoleRegisterRequest {

    @NotBlank
    @Size(max = 50)
    private String roleName;

    @NotBlank
    @Size(max = 200)
    private String roleDescription;

    public RoleRegisterRequest(String roleName, String roleDescription) {
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
