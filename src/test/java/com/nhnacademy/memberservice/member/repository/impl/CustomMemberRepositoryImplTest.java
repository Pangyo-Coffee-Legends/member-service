package com.nhnacademy.memberservice.member.repository.impl;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import com.nhnacademy.memberservice.member.dto.MemberNoResponse;
import com.nhnacademy.memberservice.member.repository.CustomMemberRepository;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({CustomMemberRepositoryImpl.class, CustomMemberRepositoryImplTest.CustomQueryDslConfig.class})
class CustomMemberRepositoryImplTest {

    @TestConfiguration
    static class CustomQueryDslConfig {
        @PersistenceContext
        private EntityManager entityManager;

        @Bean
        public JPAQueryFactory jpaQueryFactory() {
            return new JPAQueryFactory(entityManager);
        }
    }

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    @Qualifier("customMemberRepositoryImpl") // 주입 대상 명시
    private CustomMemberRepository customMemberRepository;

    @Test
    @DisplayName("findAllMemberInfo() - 회원 정보 페이징 조회")
    void findAllMemberInfo() {
        Role role = roleRepository.save(Role.ofNewRole("ROLE_ADMIN", "관리자"));

        Member member1 = Member.ofNewMember(role, "홍길동", "hong@example.com", "Test123!", "010-1234-5678");
        Member member2 = Member.ofNewMember(role, "김철수", "kim@example.com", "Test123!", "010-5678-1234");
        Member member3 = Member.ofNewMember(role, "이영희", "lee@example.com", "Test123!","010-0000-0000");

        memberRepository.saveAll(List.of(member1, member2, member3));

        PageRequest pageable = PageRequest.of(0, 2); // 1페이지당 2명

        // when
        Page<MemberInfoResponse> resultPage = customMemberRepository.findAllMemberInfo(pageable);

        // then
        assertThat(resultPage.getTotalElements()).isEqualTo(3);
        assertThat(resultPage.getTotalPages()).isEqualTo(2);
        assertThat(resultPage.getContent()).hasSize(2);

        // 데이터 값 검증
        MemberInfoResponse member = resultPage.getContent().get(0);
        assertThat(member.getName()).isNotNull();
        assertThat(member.getEmail()).contains("@example.com");
    }

    @Test
    @DisplayName("findAllMemberInfo() - 전체 회원 no 추출")
    void findAllMbNos() {
        Role role = roleRepository.save(Role.ofNewRole("ROLE_ADMIN", "관리자"));

        Member member1 = Member.ofNewMember(role, "홍길동", "hong@example.com", "Test123!", "010-1234-5678");
        Member member2 = Member.ofNewMember(role, "김철수", "kim@example.com", "Test123!", "010-5678-1234");
        Member member3 = Member.ofNewMember(role, "이영희", "lee@example.com", "Test123!","010-0000-0000");

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);


        // when
        List<MemberNoResponse> mbNos = customMemberRepository.findAllMbNos();

        // then
        assertThat(mbNos).hasSize(3);
        assertThat(mbNos).extracting("no")
                .contains(member1.getMbNo(), member2.getMbNo(), member3.getMbNo());
    }
}