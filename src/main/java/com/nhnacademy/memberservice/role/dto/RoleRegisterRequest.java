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

    /**
     * 권한의 이름입니다. 예: "USER", "ADMIN".
     * <p>
     * 이 필드는 비어 있을 수 없으며, 최대 50자까지 허용됩니다.
     * </p>
     */
    @NotBlank
    @Size(max = 50)
    private String roleName;

    /**
     * 권한에 대한 설명입니다.
     * <p>
     * 예: "일반 사용자 권한", "관리자 권한" 등.
     * 이 필드는 비어 있을 수 없으며, 최대 200자까지 허용됩니다.
     * </p>
     */
    @NotBlank
    @Size(max = 200)
    private String roleDescription;

    /**
     * 권한 이름과 권한 설명을 설정하는 생성자입니다.
     *
     * @param roleName 권한 이름 (예: "USER", "ADMIN")
     * @param roleDescription 권한 설명 (예: "일반 사용자 권한", "관리자 권한")
     */
    public RoleRegisterRequest(String roleName, String roleDescription){
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    /**
     * 권한 이름을 반환합니다.
     *
     * @return 권한의 이름
     */
    public String getRoleName() {
        return roleName;
    }

    /**
     * 권한 설명을 반환합니다.
     *
     * @return 권한에 대한 설명
     */
    public String getRoleDescription() {
        return roleDescription;
    }
}
