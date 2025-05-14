package com.nhnacademy.memberservice.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@EqualsAndHashCode
public class MemberConfirmPasswordRequest {

    @ToString.Exclude
    @NotBlank
    private String password;
    public MemberConfirmPasswordRequest(
            String password
    ) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

}
