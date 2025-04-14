package com.nhnacademy.memberservice.role.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 권한 정보를 담는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 권한 설명 수정 시 사용됩니다.
 * </p>
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class RoleUpdateRequest {

    private Long roleNo;

    private String roleDescription;

    public RoleUpdateRequest(Long roleNo, String roleDescription) {
        this.roleNo = roleNo;
        this.roleDescription = roleDescription;
    }

    public Long getRoleNo() {
        return roleNo;
    }

    public String getRoleDescription() {
        return roleDescription;
    }
}
