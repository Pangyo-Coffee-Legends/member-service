package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdatePasswordRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MemberService memberService;
    @Autowired
    private RoleRepository roleRepository;

    private Long mbNo;
    private String email = "test@example1.com";
    private Role savedRole;

    @BeforeEach
    void setUp() {
        savedRole = roleRepository.save(Role.ofNewRole("USER", "일반 사용자"));

        MemberRegisterRequest request = new MemberRegisterRequest(
                savedRole,
                "김미성",
                email,
                "password",
                "010-1234-5678",
                "password"
        );
        mbNo = memberService.registerMember(request).getMbNo();
    }

    @Test
    @DisplayName("1. 회원 조회 성공")
    void testGetMemberByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/members/email/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value(email));
    }

    @Test
    @DisplayName("2. 회원 수정 성공")
    void testUpdateMember() throws Exception {
        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                savedRole,
                "김미성",
                "update@example.com",
                "newpassword",
                "newpassword",
                "010-0000-0000"
        );

        mockMvc.perform(put("/api/v1/members/" + mbNo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbEmail").value("update@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-0000-0000"));
    }

    @Test
    @DisplayName("3. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/" + mbNo))
                .andExpect(status().isNoContent());
    }
}
