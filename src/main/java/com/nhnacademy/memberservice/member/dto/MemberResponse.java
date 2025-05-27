package com.nhnacademy.memberservice.member.dto;

import lombok.*;

/**
 * 회원 정보를 조회하는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 클라이언트가 회원의 정보를 조회할 때 응답 형식으로 사용되며,
 * 회원의 역할, 이름, 이메일, 전화번호 등의 정보를 포함합니다.
 * </p>
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberResponse {
    private Long no;

    private String roleName;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;
}
