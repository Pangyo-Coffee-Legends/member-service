package com.nhnacademy.memberservice.member.dto;


import com.nhnacademy.memberservice.role.domain.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 회원 정보를 담는 DTO 클래스입니다.
 * <p>
 * 이 클래스는 회원 정보 수정 시 사용됩니다.
 * 회원의 고유 식별자, 이름, 이메일, 전화번호, 역할 및 비밀번호 정보를 포함합니다.
 * </p>
 */
@NoArgsConstructor
@EqualsAndHashCode
@Getter
public class MemberUpdateRequest {

    /**
     * 수정 불가한 회원 고유 식별자입니다.
     * <p>
     * 이 값은 서버에서 관리하며, 클라이언트가 변경할 수 없습니다.
     * 수정 대상 회원을 식별하기 위한 용도로 사용됩니다.
     * </p>
     */
    @Getter
    private Long mbNo;

    /**
     * 회원의 역할(Role)을 나타냅니다.
     * <p>
     * 예: {@code USER}, {@code ADMIN}
     * 이 값은 사용자의 권한을 결정하며, 시스템 내에서 접근 권한 등을 제어하는 데 사용됩니다.
     * </p>
     */
    private Role role;

    /**
     * 회원의 이름입니다.
     * <p>
     * 공백일 수 없으며 최대 50자까지 허용됩니다.
     * </p>
     */
    @NotBlank
    @Size(max = 50)
    private String name;

    /**
     * 회원의 이메일 주소입니다.
     * <p>
     * 이메일 형식을 따라야 하며, 공백일 수 없고 최대 100자까지 허용됩니다.
     * </p>
     */
    @NotBlank
    @Email
    @Size(max = 100)
    private String email;

    /**
     * 회원의 비밀번호입니다.
     * <p>
     * 최소 8자 이상, 최대 200자 이하의 문자열이어야 하며 공백일 수 없습니다.
     * </p>
     */
    @NotBlank
    @Size(min = 8, max = 200)
    private String password;

    /**
     * 비밀번호 재확인 필드입니다.
     * <p>
     * {@code password} 필드와 일치하는지를 확인하기 위한 용도로 사용됩니다.
     * 최소 8자 이상, 최대 200자 이하의 문자열이어야 하며 공백일 수 없습니다.
     * </p>
     */
    @NotBlank
    @Size(min = 8, max = 200)
    private String confirmPassword;

    /**
     * 회원의 전화번호입니다.
     * <p>
     * 공백일 수 없으며, 최대 15자까지 허용됩니다.
     * </p>
     */
    @NotBlank
    @Size(max = 15)
    private String phoneNumber;

    public MemberUpdateRequest(Long mbNo, Role role, String name, String email, String password, String confirmPassword, String phoneNumber) {
        this.mbNo = mbNo;
        this.role = role;
        this.name = name;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 비밀번호와 비밀번호 재확인 필드가 일치하는지를 검증합니다.
     *
     * @return 비밀번호와 비밀번호 확인 값이 일치하면 {@code true}, 아니면 {@code false}
     */
    public boolean isPasswordValid() {
        return password.equals(confirmPassword);
    }

    public Role getRole() {
        return role;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
