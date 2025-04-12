package com.nhnacademy.memberservice.member.service;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.exception.MemberNotFoundException;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.member.service.impl.MemberServiceImpl;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * {@link MemberServiceImpl}에 대한 단위 테스트 클래스입니다.
 * 회원 등록, 조회, 수정, 삭제 로직을 검증합니다.
 */
@SpringBootTest
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Role userRole;
    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userRole = new Role(1L, "USER", "유저 권한입니다.");

        member = Member.ofNewMember("김미성", "test@nhnacademy.com", "password", "010-0000-0000");
        member.assignRole(userRole);
        ReflectionTestUtils.setField(member, "mbNo", 1L); // ID 강제 주입
    }

    @Test
    @DisplayName("1. 회원 등록 성공 테스트")
    void testRegisterMember() {
        // given
        MemberRegisterRequest request = new MemberRegisterRequest(
                userRole,
                "김미성",
                "test@example.com",
                "password",
                "010-0000-0000",
                "password"
        );

        Member save = Member.ofNewMember(request.getName(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        save.assignRole(userRole);
        ReflectionTestUtils.setField(save, "mbNo", 1L);

        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(userRole));
        when(memberRepository.save(any(Member.class))).thenReturn(save);

        // when
        MemberResponse response = memberService.registerMember(request);


        // then
        assertThat(response).isNotNull();
        assertThat(response.getMbName()).isEqualTo("김미성");
        assertThat(response.getMbEmail()).isEqualTo("test@example.com");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("2. 회원 조회 성공 테스트")
    void testGetMember_found() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        MemberResponse response = memberService.getMember(1L);

        // then
        assertThat(response.getMbEmail()).isEqualTo("test@nhnacademy.com");
    }

    @Test
    @DisplayName("3. 존재하지 않는 회원 조회 실패 테스트")
    void testGetMember_notFound() {
        // given
        when(memberRepository.findById(99L)).thenReturn(Optional.empty());

        // then
        assertThrows(MemberNotFoundException.class, () -> memberService.getMember(99L));
    }

    @Test
    @DisplayName("4. 회원 정보 수정 성공 테스트")
    void testUpdateMember() {
        // given
        MemberUpdateRequest request = new MemberUpdateRequest(
                1L,
                userRole,
                "김미성",
                "update@nhnacademy.com",
                "newpass",
                "newpass",
                "010-1111-2222"
        );

        Member updated = Member.ofNewMember(
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber()
        );
        updated.assignRole(userRole);
        ReflectionTestUtils.setField(updated, "mbNo", 1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(roleRepository.findByRoleName("USER")).thenReturn(Optional.of(userRole));
        when(memberRepository.save(any(Member.class))).thenReturn(updated);

        // when
        MemberResponse response = memberService.updateMember(request);

        // then
        assertThat(response.getMbName()).isEqualTo("김미성");
        assertThat(response.getMbEmail()).isEqualTo("update@nhnacademy.com");
        verify(memberRepository).save(any(Member.class));
    }

    @Test
    @DisplayName("5. 회원 탈퇴(소프트 삭제) 테스트")
    void testDeleteMember() {
        // given
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        memberService.deleteMember(1L);

        // then
        assertThat(member.isWithdrawn()).isTrue();
    }

    @Test
    @DisplayName("6. 존재하지 않는 회원 삭제 실패 테스트")
    void testDeleteMember_notFound() {
        // given
        when(memberRepository.findById(123L)).thenReturn(Optional.empty());

        // then
        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(123L));
    }
}
