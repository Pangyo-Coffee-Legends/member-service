package com.nhnacademy.memberservice.member.service;

import com.nhnacademy.memberservice.member.dto.*;
import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.exception.MemberEmailNotFoundException;
import com.nhnacademy.memberservice.member.exception.MemberNotFoundException;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.member.service.impl.MemberServiceImpl;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link MemberServiceImpl} 클래스의 단위 테스트 클래스입니다.
 * 회원 등록, 조회, 삭제, 비밀번호 수정 및 요약 정보 페이징 조회 등을 검증합니다.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private MemberServiceImpl memberService;

    private Role mockRole;
    private Member member;

    /**
     * 테스트 전 공통으로 사용할 mock 객체 초기화.
     */
    @BeforeEach
    void setUp() {
        mockRole = Role.ofNewRole("ROLE_USER", "일반 회원 권한");
        ReflectionTestUtils.setField(mockRole, "roleNo", 1L);

        member = Member.ofNewMember(mockRole, "김미성", "test@example.com", "password", "010-0000-0000");
        ReflectionTestUtils.setField(member, "mbNo", 1L);
    }

    /**
     * 회원 등록 성공 테스트
     */
    @Test
    @DisplayName("1. 회원 등록 성공 테스트")
    void testRegisterMember() {
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(mockRole));
        when(passwordEncoder.encode("123Asd!@#")).thenReturn("encodedPassword");

        MemberRegisterRequest request = new MemberRegisterRequest(
                "ROLE_USER", "marco", "test@example.com", "123Asd!@#", "123Asd!@#", "010-1111-2222");

        Member savedMember = Member.ofNewMember(mockRole, request.getName(), request.getEmail(), "encodedPassword", request.getPhoneNumber());
        ReflectionTestUtils.setField(savedMember, "mbNo", 1L);

        when(memberRepository.save(any())).thenReturn(savedMember);

        MemberResponse response = memberService.registerMember(request);

        assertNotNull(response);
        assertEquals("marco", response.getName());
        assertEquals("test@example.com", response.getEmail());
    }

    /**
     * 회원 이메일 조회 성공 테스트
     */
    @Test
    @DisplayName("2. 회원 조회 성공 테스트")
    void testGetMember_found() {
        when(memberRepository.findByMbEmail(anyString())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMemberByEmail(member.getMbEmail());

        assertNotNull(response);
        assertEquals("김미성", response.getName());
    }

    /**
     * 존재하지 않는 이메일로 조회 시 예외 발생 테스트
     */
    @Test
    @DisplayName("3. 존재하지 않는 회원 조회 시 예외 발생")
    void testGetMember_notFound() {
        when(memberRepository.findByMbEmail("none@test.com")).thenReturn(Optional.empty());

        assertThrows(MemberEmailNotFoundException.class, () -> memberService.getMemberByEmail("none@test.com"));
    }

    /**
     * 회원 탈퇴 처리 성공 테스트
     */
    @Test
    @DisplayName("4. 회원 탈퇴 성공 테스트")
    void testDeleteMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        assertNotNull(member.getWithdrawnAt());
    }

    /**
     * 존재하지 않는 회원 탈퇴 요청 시 예외 발생 테스트
     */
    @Test
    @DisplayName("5. 존재하지 않는 회원 탈퇴 요청 시 예외 발생")
    void testDeleteMember_notFound() {
        when(memberRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(999L));
    }

    /**
     * 비밀번호 변경 성공 테스트
     */
    @Test
    @DisplayName("6. 비밀번호 변경 성공 테스트")
    void testUpdatePassword() {
        MemberUpdatePasswordRequest request = new MemberUpdatePasswordRequest("password", "newPass123!", "newPass123!");

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(passwordEncoder.encode("password")).thenReturn("encodedOldPassword");
        when(passwordEncoder.encode("newPass123!")).thenReturn("encodedNewPassword");

        // 기존 비밀번호 비교가 password.equals(password)로 되어 있으므로 테스트 간단화를 위해 수동 설정
        memberService.updatePassword(1L, request);

        assertEquals("encodedNewPassword", member.getMbPassword());
    }

    /**
     * 회원 요약 정보 페이징 조회 테스트
     */
    @Test
    @DisplayName("7. 회원 요약 정보 페이징 조회 성공 테스트")
    void testGetMemberInfoList() {
        Pageable pageable = PageRequest.of(0, 10);
        List<MemberInfoResponse> memberList = List.of(
                new MemberInfoResponse(1L, "김미성", "test1@example.com", "010-1111-1111"),
                new MemberInfoResponse(2L, "홍길동", "test2@example.com", "010-2222-2222")
        );
        Page<MemberInfoResponse> page = new PageImpl<>(memberList, pageable, 2);

        when(memberRepository.findAllMemberInfo(pageable)).thenReturn(page);

        Page<MemberInfoResponse> result = memberService.getMemberInfoList(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("김미성", result.getContent().get(0).getName());
    }

    @Test
    @DisplayName("8. 모든 회원 no 추출 테스트")
    void getAllMemberIds() {
        List<MemberNoResponse> noResponse = List.of(
                new MemberNoResponse(1L), new MemberNoResponse(2L)
        );

        when(memberRepository.findAllMbNos()).thenReturn(noResponse);

        List<MemberNoResponse> result = memberService.getAllMemberIds();

        assertThat(result).hasSize(2);
    }
}
