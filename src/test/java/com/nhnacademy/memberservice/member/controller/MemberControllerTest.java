package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.*;
import com.nhnacademy.memberservice.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MemberController 클래스에 대한 단위 테스트 클래스입니다.
 * 회원 등록, 조회, 수정, 삭제, 비밀번호 업데이트에 대한 테스트를 포함합니다.
 */
@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    MemberService memberService;

    private ObjectMapper objectMapper;
    private MemberRegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        registerRequest = new MemberRegisterRequest(
                "role",
                "김미성",
                "test@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1234-5678"
        );
    }

    /**
     * 회원 등록 요청에 대해 HTTP 201 응답과 등록된 회원 정보를 반환하는지 검증합니다.
     */
    @Test
    @DisplayName("1. 회원 등록 성공")
    void testRegisterMember() throws Exception {
        MemberResponse mockResponse = new MemberResponse(
                1L,
                "ROLE_USER",
                "김미성",
                "test@example.com",
                "Test123!",
                "010-1234-5678"
        );

        when(memberService.registerMember(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"));

        verify(memberService).registerMember(any());
    }

    /**
     * 이메일 기반 회원 조회 요청에 대해 HTTP 200 응답과 회원 정보를 반환하는지 검증합니다.
     */
    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMemberByEmail() throws Exception {
        MemberResponse response = new MemberResponse(
                1L,
                "ROLE_USER",
                "김미성",
                "test@example.com",
                "Test123!",
                "010-1234-5678"
        );

        when(memberService.getMemberByEmail("test@example.com")).thenReturn(response);

        mockMvc.perform(get("/api/v1/members/email/test@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test@example.com"));

        verify(memberService).getMemberByEmail("test@example.com");
    }

    /**
     * 회원 정보 수정 요청에 대해 HTTP 200 응답과 수정된 정보를 반환하는지 검증합니다.
     */
    @Test
    @DisplayName("3. 회원 수정 성공")
    void testUpdateMember() throws Exception {
        long mbNo = 1L;
        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                "김미성",
                "update@example.com",
                "newpassword",
                "newpassword",
                "010-0000-0000"
        );

        MemberResponse stubResponse = new MemberResponse(
                1L,
                "ROLE_USER",
                "김미성",
                "update@example.com",
                "newpassword",
                "010-0000-0000"
        );

        when(memberService.updateMember(eq(mbNo), any(MemberUpdateRequest.class)))
                .thenReturn(stubResponse);

        mockMvc.perform(put("/api/v1/members/{mbNo}", mbNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-0000-0000"));

        verify(memberService).updateMember(eq(mbNo), any(MemberUpdateRequest.class));
    }

    /**
     * 회원 탈퇴 요청에 대해 HTTP 204 응답이 반환되는지 검증합니다.
     */
    @Test
    @DisplayName("4. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/1"))
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(1L);
    }

    /**
     * 회원 비밀번호 수정 요청에 대해 HTTP 204 응답이 반환되고,
     * 전달된 요청 값이 서비스 메서드에 정확히 전달되는지 검증합니다.
     */
    @Test
    @DisplayName("5. 회원 비밀번호 업데이트 성공")
    void testUpdatePassword() throws Exception {
        Long mbNo = 1L;

        String requestBody = """
        {
          "oldPassword": "12345678",
          "newPassword": "newsecurepass",
          "newConfirmPassword": "newsecurepass"
        }
        """;

        mockMvc.perform(put("/api/v1/members/{mbNo}/password", mbNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isNoContent());

        ArgumentCaptor<MemberUpdatePasswordRequest> captor = ArgumentCaptor.forClass(MemberUpdatePasswordRequest.class);
        verify(memberService).updatePassword(eq(mbNo), captor.capture());

        MemberUpdatePasswordRequest capturedRequest = captor.getValue();
        assertThat(capturedRequest.getOldPassword()).isEqualTo("12345678");
        assertThat(capturedRequest.getNewPassword()).isEqualTo("newsecurepass");
    }

    /**
     * 회원 목록 조회 요청에 대해 페이징된 MemberPageResponse 객체를 반환하는지 검증합니다.
     */
    @Test
    @DisplayName("6. 회원 목록 페이징 조회 성공")
    void testGetMemberInfoList() throws Exception {
        List<MemberInfoResponse> memberList = List.of(
                new MemberInfoResponse(1L, "김미성", "test1@example.com", "010-1234-5678", "ROLE_USER"),
                new MemberInfoResponse(2L, "홍길동", "test2@example.com", "010-0000-0000", "ROLE_USER")
        );

        Page<MemberInfoResponse> mockPage = new PageImpl<>(memberList, PageRequest.of(0, 10), 2);

        when(memberService.getMemberInfoList(any(Pageable.class))).thenReturn(mockPage);

        mockMvc.perform(get("/api/v1/members")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.currentPage").value(0));

        verify(memberService).getMemberInfoList(any(Pageable.class));
    }

    @Test
    @DisplayName("회원정보 조회 - email")
    void getMemberInfo_email() throws Exception {
        MemberInfoResponse response = new MemberInfoResponse(1L, "홍길동", "test@test.com", "010-1111-2222", "ROLE_USER");
        when(memberService.getMemberInfoByEmail(Mockito.anyString())).thenReturn(response);
        mockMvc.perform(get("/api/v1/members/email/{mbEmail}/info", "test@test.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.no").value(1))
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1111-2222"))
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"))
                .andDo(print());
    }

    @Test
    @DisplayName("회원정보 조회 - 회원번호")
    void getMemberInfo_no() throws Exception {
        MemberInfoResponse response = new MemberInfoResponse(1L, "홍길동", "test@test.com", "010-1111-2222", "ROLE_USER");
        when(memberService.getMemberInfo(Mockito.anyLong())).thenReturn(response);
        mockMvc.perform(get("/api/v1/members/{no}/info", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("홍길동"))
                .andExpect(jsonPath("$.email").value("test@test.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1111-2222"))
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"))
                .andDo(print());
    }

    @Test
    @DisplayName("비밀번호 확인")
    void verify() throws Exception {
        MemberConfirmPasswordRequest request = new MemberConfirmPasswordRequest("Test123!");
        String body = objectMapper.writeValueAsString(request);
        when(memberService.verify(1L, request)).thenReturn(true);

        mockMvc.perform(
                post("/api/v1/members/{no}/password", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }
}
