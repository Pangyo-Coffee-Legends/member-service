package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerIntegrationTest {

    Role savedRole;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private RoleRepository roleRepository;
    private MemberRegisterRequest registerRequest;


    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        savedRole = roleRepository.save(Role.ofNewRole("USER", "일반 사용자"));
        registerRequest = new MemberRegisterRequest(
                savedRole,
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

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"));

    }

    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMemberByEmail() throws Exception {
        String email = "test@example.com";
        mockMvc.perform(get("/api/v1/members/email/"+email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value(email));

    }

    @Test
    @DisplayName("3. 회원 수정 성공")
    void testUpdateMember() throws Exception {
        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
                savedRole,
                "김미성",
                "update@example.com",
                "newpassword",
                "newpassword",
                "010-0000-0000"
        );
        mockMvc.perform(put("/api/v1/members/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbEmail").value("update@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-0000-0000"));
    }

    @Test
    @DisplayName("4. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/1" +
                        ""))
                .andExpect(status().isNoContent());

    }


}