package com.nhnacademy.memberservice.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberConfirmPasswordRequest {

    @ToString.Exclude
    @NotBlank
    private String password;

}
