package com.nhnacademy.memberservice.member.dto;

import lombok.*;

/**
 * 회원 고유 번호(no)를 응답으로 전달하기 위한 DTO 클래스입니다.
 *
 * <p>회원 등록, 조회 등의 작업 이후 클라이언트에게 회원 식별자 값을 반환할 때 사용됩니다.</p>
 *
 * <ul>
 *   <li>{@code no} 필드는 회원의 고유 식별 번호를 의미합니다.</li>
 *   <li>{@link lombok.ToString}을 통해 toString() 구현이 자동 생성되며, {@link lombok.EqualsAndHashCode}를 통해 동등성 비교 기능도 포함됩니다.</li>
 *   <li>{@link lombok.Getter}, {@link lombok.NoArgsConstructor}, {@link lombok.AllArgsConstructor}를 사용해 보일러플레이트 코드를 제거했습니다.</li>
 * </ul>
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberNoResponse {
      private Long no;
}
