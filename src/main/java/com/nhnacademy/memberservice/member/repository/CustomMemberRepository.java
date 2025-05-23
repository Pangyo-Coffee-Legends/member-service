package com.nhnacademy.memberservice.member.repository;

import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import com.nhnacademy.memberservice.member.dto.MemberNoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * QueryDSL 기반 커스텀 쿼리 인터페이스입니다.
 */
public interface CustomMemberRepository {
    /**
     * 전체 회원 요약 정보를 페이징하여 조회합니다.
     * <p>
     * 회원 번호, 이름, 이메일, 전화번호를 포함하는 DTO 리스트를 반환하며,
     * 전체 회원 수를 함께 조회하여 {@link PageImpl}로 감싸 반환합니다.
     * </p>
     *
     * @param pageable 페이지 번호, 크기, 정렬 조건 등이 포함된 Spring Data {@link Pageable} 객체
     * @return {@link MemberInfoResponse} 객체를 담은 페이징 결과 {@link Page}
     */
    Page<MemberInfoResponse> findAllMemberInfo(Pageable pageable);

    /**
     * 전체 회원의 고유 번호(MbNo) 목록을 조회합니다.
     *
     * @return 회원 고유 번호를 담은 {@link MemberNoResponse} 리스트
     */
    List<MemberNoResponse> findAllMbNos();

}
