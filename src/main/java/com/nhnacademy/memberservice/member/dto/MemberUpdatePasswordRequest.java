package com.nhnacademy.memberservice.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberUpdatePasswordRequest {

    @ToString.Exclude
    @NotBlank
    @Size(min = 8, max = 200)
    private String oldPassword;


    @ToString.Exclude
    @NotBlank
    @Size(min = 8, max = 200)
    private String newPassword;

    @ToString.Exclude
    @NotBlank
    @Size(min = 8, max = 200)
    private String newConfirmPassword;
    /**
     * 비밀번호와 비밀번호 재확인이 일치하는지를 검증합니다.
     *
     * @return 두 필드가 동일하면 {@code true}, 그렇지 않으면 {@code false}
     */
    public boolean isPasswordValid() {
        return newPassword.equals(newConfirmPassword);
    }

}
