package com.nhnacademy.memberservice.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원 가입 요청을 담는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 새로운 사용자의 회원 가입 요청 정보를 전달할 때 사용되며,
 * 일반 사용자 또는 관리자인지를 구분할 수 있는 역할 정보와
 * 이름, 이메일, 비밀번호, 전화번호 등의 필드를 포함합니다.
 * 또한, 비밀번호 재확인 기능을 통해 클라이언트 측에서 일치 여부를 사전에 검증할 수 있도록 지원합니다.
 * </p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class MemberRegisterRequest {

    @NotBlank
    @Size(max = 50)
    private String roleName;

    @NotBlank
    @Size(max = 50)
    private String name;

    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Size(max = 100)
    private String email;

    @ToString.Exclude
    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?]).{8,20}$",
            message = "비밀번호는 8~20자이며, 소문자, 대문자, 숫자, 특수문자를 각각 하나 이상 포함해야 합니다."
    )
    @Size(min = 8, max = 20)
    private String password;

    @ToString.Exclude
    @NotBlank
    @Size(min = 8, max = 20)
    private String confirmPassword;

    @NotBlank
    @Pattern(
            regexp = "^010-\\d{4}-\\d{4}$",
            message = "전화번호는 010-xxxx-xxxx 형식이어야 합니다."
    )
    @Size(max = 15)
    private String phoneNumber;

    /**
     * 비밀번호와 비밀번호 재확인이 일치하는지를 검증합니다.
     *
     * @return 두 필드가 동일하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean isPasswordValid() {
        return password.equals(confirmPassword);
    }

}
