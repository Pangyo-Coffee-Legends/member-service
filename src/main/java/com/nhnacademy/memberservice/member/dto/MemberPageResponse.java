package com.nhnacademy.memberservice.member.dto;

import lombok.*;

import java.util.List;

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

