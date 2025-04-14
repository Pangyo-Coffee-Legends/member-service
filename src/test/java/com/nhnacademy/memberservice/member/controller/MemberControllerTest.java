package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
class MemberControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    private MemberController memberController;

    @Mock
    private MemberService memberService;

    private ObjectMapper objectMapper;


    private MemberRegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
        objectMapper = new ObjectMapper();

        registerRequest = new MemberRegisterRequest(
                Role.ofNewRole("USER", "일반 사용자"),
                "김미성",
                "test@example.com",
                "password",
                "010-1234-5678",
                "password"
        );
    }

    @Test
    @DisplayName("1. 회원 등록 성공")
    void testRegisterMember() throws Exception {
        MemberResponse response = new MemberResponse(
                registerRequest.getRole(),
                registerRequest.getName(),
                registerRequest.getEmail(),
                registerRequest.getPhoneNumber()
        );

        when(memberService.registerMember(any())).thenReturn(response);

        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"));

        verify(memberService).registerMember(any());
    }

    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMember() throws Exception {
        MemberResponse response = new MemberResponse(
                Role.ofNewRole("USER", "일반 사용자"),
                "김미성",
                "test@example.com",
                "010-1234-5678"
        );

        when(memberService.getMember(1L)).thenReturn(response);

        mockMvc.perform(get("/api/v1/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value("test@example.com"));

        verify(memberService).getMember(1L);
    }

    @Test
    @DisplayName("3. 회원 수정 성공")
    void testUpdateMember() throws Exception {
        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                1L,
                Role.ofNewRole("USER", "일반 사용자"),
                "김미성",
                "update@example.com",
                "newpassword",
                "newpassword",
                "010-0000-0000"
        );

        MemberResponse response = new MemberResponse(
                updateRequest.getRole(),
                updateRequest.getName(),
                updateRequest.getEmail(),
                updateRequest.getPhoneNumber()
        );

        when(memberService.updateMember(any())).thenReturn(response);

        mockMvc.perform(put("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbEmail").value("update@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-0000-0000"));

        verify(memberService).updateMember(any());
    }

    @Test
    @DisplayName("4. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/1"))
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(1L);
    }
}
