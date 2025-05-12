package com.nhnacademy.memberservice.member.repository;

import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * QueryDSL 기반 커스텀 쿼리 인터페이스입니다.
 */
public interface CustomMemberRepository {
    /**
     * 회원 요약 정보(MemberInfoResponse)를 한번의 쿼리로 조회합니다.
     *
     * @return 회원 정보 리스트
     */
    Page<MemberInfoResponse> findAllMemberInfo(Pageable pageable);

    /**
     * 회원 고유 번호(MbNo)를 한번의 쿼리로 조회합니다.
     *
     * @return 회원 정보 리스트
     */
    List<Long> findAllMbNos();

}
