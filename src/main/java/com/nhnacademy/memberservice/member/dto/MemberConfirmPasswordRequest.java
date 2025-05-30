package com.nhnacademy.memberservice.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 사용자의 비밀번호 확인 요청을 나타내는 DTO 클래스입니다.
 *
 * <p>비밀번호 재입력 또는 민감한 작업 수행 전 비밀번호 확인이 필요한 API에서 사용됩니다.</p>
 *
 * <ul>
 *   <li>{@code password} 필드는 필수 입력이며, 공백일 수 없습니다.</li>
 *   <li>{@link ToString.Exclude}를 통해 로그 등에서 password가 노출되지 않도록 설정되어 있습니다.</li>
 * </ul>
 *
 * <p>해당 클래스는 {@link lombok.Getter}, {@link lombok.NoArgsConstructor},
 * {@link lombok.AllArgsConstructor}, {@link lombok.EqualsAndHashCode} 등을 사용하여
 * 불필요한 보일러플레이트 코드를 줄였습니다.</p>
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberConfirmPasswordRequest {

    @ToString.Exclude
    @NotBlank
    private String password;

}
