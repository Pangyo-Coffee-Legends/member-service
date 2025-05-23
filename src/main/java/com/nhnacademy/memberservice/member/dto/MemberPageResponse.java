package com.nhnacademy.memberservice.member.dto;

import lombok.*;

import java.util.List;

/**
 * 회원 목록 조회 결과를 페이징 형태로 제공하기 위한 응답 DTO입니다.
 *
 * <p>회원 리스트를 페이지 단위로 조회할 때, 각 페이지의 콘텐츠와 페이징 메타 정보를 함께 제공합니다.</p>
 *
 * <ul>
 *   <li>{@code content} : 현재 페이지에 포함된 회원 정보 리스트</li>
 *   <li>{@code totalPages} : 전체 페이지 수</li>
 *   <li>{@code totalElements} : 전체 회원 수</li>
 *   <li>{@code currentPage} : 현재 페이지 번호 (0부터 시작)</li>
 * </ul>
 *
 * <p>{@link lombok.Getter}, {@link lombok.ToString}, {@link lombok.EqualsAndHashCode},
 * {@link lombok.NoArgsConstructor}, {@link lombok.AllArgsConstructor} 등을 사용하여
 * 코드 간결성과 유지보수성을 높였습니다.</p>
 */
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class MemberPageResponse {
        private List<MemberInfoResponse> content;
        private int totalPages;
        private long totalElements;
        private int currentPage;
}

