package com.nhnacademy.memberservice.member.repository.impl;

import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import com.nhnacademy.memberservice.member.dto.MemberNoResponse;
import com.nhnacademy.memberservice.member.domain.QMember;
import com.nhnacademy.memberservice.member.dto.QMemberInfoResponse;
import com.nhnacademy.memberservice.member.repository.CustomMemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

/**
 * 회원 리포지토리의 사용자 정의 구현체입니다.
 * <p>
 * QueryDSL을 활용하여 페이징된 회원 요약 정보를 조회합니다.
 * </p>
 */
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

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
    @Override
    public Page<MemberInfoResponse> findAllMemberInfo(Pageable pageable) {
        QMember member = QMember.member;

        // 실제 데이터 조회
        List<MemberInfoResponse> content = queryFactory
                .select(
                        new QMemberInfoResponse(
                        member.mbNo,
                        member.mbName,
                        member.mbEmail,
                        member.phoneNumber,
                        member.role.roleName
                ))
                .from(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 회원 수 조회 (null 처리 포함)
        Long total = queryFactory
                .select(member.count())
                .from(member)
                .fetchOne();

        return PageableExecutionUtils.getPage(content, pageable, () -> total);
    }
    /**
     * 회원 고유 번호(MbNo)를 조회합니다.
     * 여기 채워야 함----
     * @return 회원 정보 리스트
     */
    @Override
    public List<MemberNoResponse> findAllMbNos(){
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(
                    MemberNoResponse.class, member.mbNo)
                )
                .from(member)
                .fetch();
    }
}