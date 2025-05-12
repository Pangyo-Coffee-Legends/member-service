package com.nhnacademy.memberservice.member.dto;

import lombok.Value;

import java.util.List;

@Value
public class MemberPageResponse {
        List<MemberInfoResponse> content;
        int totalPages;
        long totalElements;
        int currentPage;
}

