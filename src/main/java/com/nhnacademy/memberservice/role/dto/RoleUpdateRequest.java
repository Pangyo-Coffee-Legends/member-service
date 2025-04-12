package com.nhnacademy.memberservice.role.dto;

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
    /**
     * 수정 불가한 권한 고유 식별자입니다.
     * 이 값은 클라이언트에서 변경할 수 없습니다.
     */
    private Long roleNo;

    /**
     * 수정할 권한 설명입니다.
     * <p>
     * 관리자가 권한에 대한 상세 설명을 갱신하고자 할 때 사용됩니다.
     * 예: "일반 사용자 권한", "시스템 관리자 권한" 등.
     * </p>
     */
    private String roleDescription;

    public Long getRoleNo() {
        return roleNo;
    }

    public String getRoleDescription() {
        return roleDescription;
    }
}
