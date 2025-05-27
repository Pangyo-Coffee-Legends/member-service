package com.nhnacademy.memberservice.member.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원 정보를 담는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 회원 정보 수정 시 사용됩니다.
 * 회원의 이름, 이메일, 역할 및 비밀번호 정보를 포함합니다.
 * </p>
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberUpdateRequest {


    @NotBlank
    @Size(max = 50)
    private String name;

    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    @NotBlank
    @Size(max = 15)
    private String phoneNumber;
}
