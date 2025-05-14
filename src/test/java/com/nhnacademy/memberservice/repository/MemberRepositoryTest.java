package com.nhnacademy.memberservice.repository;

import com.nhnacademy.memberservice.config.QuerydslConfig;
import com.nhnacademy.memberservice.member.dto.MemberInfoResponse;
import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
@Import(QuerydslConfig.class)
@DataJpaTest
@ActiveProfiles("test")
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private EntityManager entityManager;

    /**
     * 테스트용 회원 데이터를 미리 저장합니다.
     */
    @BeforeEach
    void setup() {
        Role role = Role.ofNewRole("USER","유저");
        roleRepository.save(role);

        IntStream.rangeClosed(1, 30)
                .forEach(i -> {
                    Member member = Member.builder()
                            .mbName("사용자" + i)
                            .mbEmail("user" + i + "@test.com")
                            .mbPassword("1234!Abcd")
                            .phoneNumber("010-0000-0000" + i)
                            .role(role)
                            .build();
                    memberRepository.save(member);
                });
        entityManager.flush();
        entityManager.clear();
    }

    /**
     * QueryDSL 기반 회원 요약 정보 페이징 조회 테스트
     */
    @Test
    @DisplayName("QueryDSL로 회원 요약 정보 페이징 조회 성공")
    void testFindAllMemberInfoWithQueryDsl() {
        // given
        PageRequest pageRequest = PageRequest.of(0, 10);

        // when
        Page<MemberInfoResponse> result = memberRepository.findAllMemberInfo(pageRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(10);
        assertThat(result.getTotalElements()).isEqualTo(30);
        assertThat(result.getTotalPages()).isEqualTo(3);
    }

    /**
     * 이메일로 회원 존재 여부 확인 테스트
     */
    @Test
    @DisplayName("이메일로 회원 존재 여부 확인")
    void testExistsMemberByEmail() {
        boolean exists = memberRepository.existsMemberByMbEmail("user1@test.com");

        assertThat(exists).isTrue();
    }

    /**
     * 존재하지 않는 이메일로 회원 존재 여부 확인 테스트
     */
    @Test
    @DisplayName("존재하지 않는 이메일로 회원 존재 여부 확인")
    void testNotExistsMemberByEmail() {
        boolean exists = memberRepository.existsMemberByMbEmail("no-user@test.com");

        assertThat(exists).isFalse();
    }
}
