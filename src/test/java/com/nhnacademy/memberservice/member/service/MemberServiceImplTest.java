package com.nhnacademy.memberservice.member.service;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdatePasswordRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.exception.MemberEmailNotFoundException;
import com.nhnacademy.memberservice.member.exception.MemberNotFoundException;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.member.service.impl.MemberServiceImpl;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link MemberServiceImpl}에 대한 단위 테스트 클래스입니다.
 * 회원 등록, 조회, 수정, 삭제 로직을 검증합니다.
 */
@ExtendWith(MockitoExtension.class)
class MemberServiceImplTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private MemberServiceImpl memberService;

    Role mockRole;
    Member member;

    @BeforeEach
    void setUp(){
        mockRole = Role.ofNewRole("ROLE_MEMBER", "일반 회원 권한");
        ReflectionTestUtils.setField(mockRole, "roleNo", 1L);

        member = Member.ofNewMember(mockRole,  "김미성", "test@example.com",  "password", "010-0000-0000");
        ReflectionTestUtils.setField(MemberServiceImplTest.this.member, "mbNo", 1L);
    }

    @Test
    @DisplayName("1. 회원 등록 성공 테스트")
    void testRegisterMember() {

        when(roleRepository.findByRoleName("ROLE_MEMBER")).thenReturn(Optional.of(mockRole));

        MemberRegisterRequest request = new MemberRegisterRequest(
                "ROLE_MEMBER",
                "marco",
                "test@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1111-2222"
        );

        Member savedMember = Member.ofNewMember(mockRole,
                request.getName(),
                request.getEmail(),
                request.getPassword(),
                request.getPhoneNumber()
        );
        ReflectionTestUtils.setField(savedMember, "mbNo", 1L);

        when(memberRepository.save(Mockito.any())).thenReturn(savedMember);

        MemberResponse response = memberService.registerMember(request);

        verify(roleRepository, Mockito.times(1)).findByRoleName(Mockito.any());
        verify(memberRepository, Mockito.times(1)).save(Mockito.any());



        assertNotNull(response);
        assertAll(
                () -> {
                    assertThat(response.getName()).isEqualTo("marco");
                    assertThat(response.getEmail()).isEqualTo("test@example.com");
                    assertEquals(request.getPassword(), request.getConfirmPassword());
                    verify(memberRepository).save(any(Member.class));
                }

        );
    }

    @Test
    @DisplayName("2. 회원 조회 성공 테스트")
    void testGetMember_found() {

        when(memberRepository.findByMbEmail(Mockito.anyString())).thenReturn(Optional.of(member));

        MemberResponse response = memberService.getMemberByEmail(member.getMbEmail());

        verify(memberRepository, Mockito.times(1)).findByMbEmail(Mockito.anyString());
        assertNotNull(response);
    }

    @Test
    @DisplayName("3. 존재하지 않는 회원 조회 시 MemberNotFoundException 발생")
    void testGetMember_notFound() {
        String nonExistentEmail = "test@eee.com";
        when(memberRepository.findByMbEmail(nonExistentEmail)).thenReturn(Optional.empty());

        Assertions.assertThrows(MemberEmailNotFoundException.class, ()-> {
            memberService.getMemberByEmail(nonExistentEmail);
        });
    }
//
//    @Test
//    @DisplayName("4. 회원 정보 수정 성공 테스트")
//    void testUpdateMember() {
//        when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
//
//        MemberUpdateRequest updateRequest = new MemberUpdateRequest(mockRole, "이름바꿈", "test@example.com", "010-0000-0000");
//
//        MemberResponse response = memberService.updateMember(member.getMbNo(), updateRequest);
//
//        verify(memberRepository, Mockito.times(1)).findById(Mockito.anyLong());
//
//        assertNotNull(response);
//        assertThat(response.getName()).isEqualTo("이름바꿈");
//
//    }
//    @Test
//    @DisplayName("4. 회원 정보 수정 성공 테스트")
//    void testUpdateMember() {
//        when(memberRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(member));
//
////        MemberUpdateRequest updateRequest = new MemberUpdateRequest(mockRole, "이름바꿈", "test@example.com", "010-0000-0000");
//
////        MemberResponse response = memberService.updateMember(member.getMbNo(), updateRequest);
//
//        verify(memberRepository, Mockito.times(1)).findById(Mockito.anyLong());
//
//        assertNotNull(response);
//        assertThat(response.getMbName()).isEqualTo("이름바꿈");
//
//    }

    @Test
    @DisplayName("5. 회원 탈퇴(소프트 삭제) 테스트")
    void testDeleteMember() {
        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        memberService.deleteMember(1L);

        assertThat(member.getWithdrawnAt()).isNotNull();
    }

    @Test
    @DisplayName("6. 존재하지 않는 회원 삭제 실패 테스트")
    void testDeleteMember_notFound() {
        when(memberRepository.findById(123L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.deleteMember(123L));
    }

    @Test
    @DisplayName("7. 비밀번호 업데이트 테스트")
    void testUpdatePassword() {

        String oldPassword = "password";
        String newPassword = "newsecurepass";

        MemberUpdatePasswordRequest request = new MemberUpdatePasswordRequest(
                oldPassword,
                newPassword,
                newPassword
        );

        when(memberRepository.findById(member.getMbNo())).thenReturn(Optional.of(member));

        memberService.updatePassword(member.getMbNo(),request);

        assertThat(member.getMbPassword()).isEqualTo("newsecurepass");
    }

}
