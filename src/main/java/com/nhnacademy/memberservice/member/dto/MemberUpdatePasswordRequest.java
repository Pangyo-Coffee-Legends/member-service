package com.nhnacademy.memberservice.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * 회원 비밀번호 변경 요청을 처리하기 위한 DTO 클래스입니다.
 *
 * <p>기존 비밀번호와 새로운 비밀번호, 그리고 새로운 비밀번호 확인 값을 함께 받아
 * 비밀번호 변경 시 필요한 정보를 제공합니다.</p>
 *
 * <ul>
 *   <li>{@code oldPassword} : 현재 사용 중인 기존 비밀번호</li>
 *   <li>{@code newPassword} : 새로 설정할 비밀번호</li>
 *   <li>{@code newConfirmPassword} : 새 비밀번호 확인 입력값</li>
 * </ul>
 *
 * <p>각 비밀번호 필드는 최소 8자 이상, 최대 200자 이하의 제약을 가지며, 공백일 수 없습니다.
 * {@link ToString.Exclude}를 통해 toString 출력 시 민감 정보 노출을 방지합니다.</p>
 *
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberUpdatePasswordRequest {

    @NotBlank
    @Size(min = 8, max = 200)
    private String oldPassword;


    @NotBlank
    @Size(min = 8, max = 200)
    private String newPassword;

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
