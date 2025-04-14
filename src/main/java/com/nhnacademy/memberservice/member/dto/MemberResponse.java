package com.nhnacademy.memberservice.member.dto;

import com.nhnacademy.memberservice.role.domain.Role;
import lombok.*;

/**
 * 회원 정보를 조회하는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 클라이언트가 회원의 정보를 조회할 때 응답 형식으로 사용되며,
 * 회원의 역할, 이름, 이메일, 전화번호 등의 정보를 포함합니다.
 * </p>
 */
@Getter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MemberResponse {

    /**
     * 회원의 역할 정보입니다.
     * <p>
     * 예: {@code USER}, {@code ADMIN}
     * 클라이언트가 회원의 권한 수준을 확인할 수 있도록 합니다.
     * </p>
     */
    private Role role;

    /**
     * 회원의 이름입니다.
     * <p>
     * 일반적으로 회원가입 시 입력된 이름이며, 사용자 식별 시 사용됩니다.
     * </p>
     */
    private String mbName;

    /**
     * 회원의 이메일 주소입니다.
     * <p>
     * 사용자 계정과 관련된 고유한 식별자로 사용되며,
     * 로그인, 알림 발송 등 다양한 기능에 활용됩니다.
     * </p>
     */
    private String mbEmail;

    /**
     * 회원의 전화번호입니다.
     * <p>
     * 고객 지원 또는 본인 확인 등의 목적으로 사용될 수 있습니다.
     * </p>
     */
    private String phoneNumber;


    public MemberResponse(Role role, String mbName, String mbEmail, String phoneNumber) {
        this.role = role;
        this.mbName = mbName;
        this.mbEmail = mbEmail;
        this.phoneNumber = phoneNumber;
    }

}
