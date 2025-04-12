package com.nhnacademy.memberservice.role.dto;

import lombok.*;

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
@Getter
@Setter
public class RoleResponse {

    /**
     * 권한의 이름입니다.
     * <p>
     * 예: "ADMIN", "USER"
     * </p>
     */
    private String roleName;

    /**
     * 권한에 대한 설명입니다.
     * <p>
     * 예: "시스템 관리자 권한", "일반 사용자 권한"
     * </p>
     */
    private String roleDescription;



}
