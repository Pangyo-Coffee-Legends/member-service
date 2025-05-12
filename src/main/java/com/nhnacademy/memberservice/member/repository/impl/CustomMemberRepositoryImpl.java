package com.nhnacademy.memberservice.member.repository.impl;

import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import com.nhnacademy.memberservice.member.entity.QMember;
import com.nhnacademy.memberservice.member.repository.CustomMemberRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 회원 리포지토리 사용자 정의 구현 클래스입니다.
 * <p>
 * QueryDSL을 이용하여 복잡한 쿼리를 구현합니다.
 * </p>
 */
@RequiredArgsConstructor
public class CustomMemberRepositoryImpl implements CustomMemberRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * 전체 회원의 요약 정보를 조회합니다.
     * <p>
     * 회원 번호, 이름, 이메일, 전화번호를 포함한 회원 요약 정보를 리스트로 반환합니다.
     * QueryDSL을 활용하여 한 번의 쿼리로 효율적으로 데이터를 조회합니다.
     * </p>
     *
     * @return {@code MemberInfoResponse} 객체 리스트, 모든 회원의 요약 정보
     */
    @Override
    public List<MemberInfoResponse> findAllMemberInfo() {
        QMember member = QMember.member;

        return queryFactory
                .select(Projections.constructor(
                        MemberInfoResponse.class,
                        member.mbNo,
                        member.mbName,
                        member.mbEmail,
                        member.phoneNumber
                ))
                .from(member)
                .fetch();
    }

    /**
     * 회원 고유 번호(MbNo)를 조회합니다.
     * 여기 채워야 함----
     * @return 회원 정보 리스트
     */
    @Override
    public List<Long> findAllMbNos(){
        QMember member = QMember.member;

        return queryFactory
                .select(member.mbNo) // Projections 없이 필드 직접 선택
                .from(member)
                .fetch();
    }
}
