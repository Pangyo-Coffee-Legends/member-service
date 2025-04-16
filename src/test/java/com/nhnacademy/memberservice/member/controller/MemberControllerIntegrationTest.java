package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("integration")
class MemberControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Role userRole;

    @BeforeEach
    void setup() {
        userRole = roleRepository.save(Role.ofNewRole( "USER", "일반 사용자"));
    }

    @Test
    @DisplayName("1. 회원 등록 통합 테스트")
    void testRegisterMember() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest(
                userRole,
                "김미성",
                "test@example.com",
                "password",
                "010-1234-5678",
                "password"
        );

        mockMvc.perform(post("/api/v1/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value("test@example.com"));
    }

    @Test
    @DisplayName("2. 회원 조회 통합 테스트")
    void testGetMember() throws Exception {
        Member member = Member.ofNewMember("김미성", "test@example.com", "password", "010-0000-0000");
        member.assignRole(userRole);
        member = memberRepository.save(member);

        mockMvc.perform(get("/api/v1/members/" + member.getMbNo()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbName").value("김미성"));
    }

    @Test
    @DisplayName("3. 회원 수정 통합 테스트")
    void testUpdateMember() throws Exception {
        Member member = Member.ofNewMember("김미성", "old@example.com", "pass", "010-0000-0000");
        member.assignRole(userRole);
        member = memberRepository.save(member);

        MemberUpdateRequest request = new MemberUpdateRequest(
                member.getMbNo(),
                userRole,
                "김미성",
                "new@example.com",
                "newpass",
                "newpass",
                "010-9999-9999"
        );

        mockMvc.perform(put("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbEmail").value("new@example.com"));
    }

    @Test
    @DisplayName("4. 회원 삭제 통합 테스트")
    void testDeleteMember() throws Exception {
        Member member = Member.ofNewMember("김미성", "del@example.com", "password", "010-8888-8888");
        member.assignRole(userRole);
        member = memberRepository.save(member);

        mockMvc.perform(delete("/api/v1/members/" + member.getMbNo()))
                .andExpect(status().isNoContent());
    }
}
