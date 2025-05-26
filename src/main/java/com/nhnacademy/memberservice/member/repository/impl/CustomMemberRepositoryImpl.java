package com.nhnacademy.memberservice.member.repository.impl;

import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import com.nhnacademy.memberservice.member.dto.MemberNoResponse;
import com.nhnacademy.memberservice.member.domain.QMember;
import com.nhnacademy.memberservice.member.dto.QMemberInfoResponse;
import com.nhnacademy.memberservice.member.repository.CustomMemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        JPAQuery<Long> total = queryFactory
                .select(member.count())
                .from(member);

        return PageableExecutionUtils.getPage(content, pageable, total::fetchOne);
    }

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