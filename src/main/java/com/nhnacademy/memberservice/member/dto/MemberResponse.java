package com.nhnacademy.memberservice.member.dto;

import com.nhnacademy.memberservice.role.domain.Role;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 회원 정보를 조회하는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 클라이언트가 회원의 정보를 조회할 때 응답 형식으로 사용되며,
 * 회원의 역할, 이름, 이메일, 전화번호 등의 정보를 포함합니다.
 * </p>
 */
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MemberResponse {


    private Role role;


    private String mbName;


    private String mbEmail;


    private String phoneNumber;


    public MemberResponse(Role role, String mbName, String mbEmail, String phoneNumber) {
        this.role = role;
        this.mbName = mbName;
        this.mbEmail = mbEmail;
        this.phoneNumber = phoneNumber;
    }

    public Role getRole() {
        return role;
    }

    public String getMbName() {
        return mbName;
    }

    public String getMbEmail() {
        return mbEmail;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
