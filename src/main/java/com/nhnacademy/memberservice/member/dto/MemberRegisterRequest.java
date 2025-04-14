package com.nhnacademy.memberservice.member.dto;

import com.nhnacademy.memberservice.role.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class MemberRegisterRequest {

    /**
     * 회원의 역할을 지정합니다.
     * <p>
     * 예: {@code USER}, {@code ADMIN}
     * 사용자의 권한을 구분하는 데 사용되며,
     * 관리자는 별도의 권한을 통해 시스템을 운영할 수 있습니다.
     * </p>
     */
    private Role role;

    /**
     * 회원의 이름입니다.
     * <p>
     * 최대 50자까지 허용되며, 사용자 식별 및 인사말 등에 사용됩니다.
     * </p>
     */
    @NotBlank
    @Size(max = 50)
    private String name;

    /**
     * 회원의 이메일 주소입니다.
     * <p>
     * 로그인 ID로 사용될 수 있으며, 최대 100자까지 허용됩니다.
     * 형식은 {@code example@domain.com}을 따라야 합니다.
     * </p>
     */
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    /**
     * 회원의 비밀번호입니다.
     * <p>
     * 최소 8자에서 최대 200자까지 허용되며, 보안을 위해 암호화되어 저장되어야 합니다.
     * </p>
     */
    @NotBlank
    @Size(min = 8, max = 200)
    private String password;

    /**
     * 회원의 전화번호입니다.
     * <p>
     * 최대 15자까지 허용되며, 본인 인증 등의 목적으로 활용됩니다.
     * </p>
     */
    @NotBlank
    @Size(max = 15)
    private String phoneNumber;

    /**
     * 비밀번호 재확인 필드입니다.
     * <p>
     * {@code password} 필드와 일치하는지 확인하기 위해 사용되며,
     * 클라이언트에서 비밀번호 입력 실수를 방지하기 위해 필요합니다.
     * {@link ToString.Exclude}가 적용되어 문자열 출력 시 노출되지 않도록 보호됩니다.
     * </p>
     */
    @ToString.Exclude
    @NotBlank
    @Size(min = 8, max = 200)
    private String confirmPassword;

    /**
     * 비밀번호와 비밀번호 재확인이 일치하는지를 검증합니다.
     *
     * @return 두 필드가 동일하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean isPasswordValid() {
        return password.equals(confirmPassword);
    }


    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Role getRole() {
        return role;
    }
}
