package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdatePasswordRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MemberController 통합 테스트 클래스입니다.
 * 정상 흐름 + 예외 케이스 포함
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
    private String email = "test@example123.com";
    private String password = "Password!";
    private MemberRegisterRequest request;
    @BeforeEach
    void setUp() {
        request = new MemberRegisterRequest(
                "USER_MEMBER",
                "김미성",
                email,
                password,
                "010-1234-5678",
                password
        );
        mbNo = memberService.registerMember(request).getMbNo();
    }

    @Test
    @DisplayName("1. 회원 등록 성공")
    void testRegisterMember() throws Exception {
        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mbEmail").value(email))
                .andExpect(jsonPath("$.mbName").value("김미성"));
    }

    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMemberByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/members/email/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value(email));
    }

    @Test
    @DisplayName("3. 존재하지 않는 회원 조회 시 MemberNotFoundException 발생")
    void testGetMemberByEmail_notFound() throws Exception {
        mockMvc.perform(get("/api/v1/members/email/notfound@example.com"))
                .andExpect(status().isBadRequest()) // 예외 핸들러 처리 코드에 따라 404 또는 400
                .andExpect(jsonPath("$.code").value("MEMBER_EMAIL_NOT_FOUND"));
    }

    @Test
    @DisplayName("4. 회원 수정 성공")
    void testUpdateMember() throws Exception {
        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
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
    @DisplayName("5. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/" + mbNo))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("6. 존재하지 않는 회원 삭제 실패 테스트")
    void testDeleteMember_notFound() throws Exception {
        mockMvc.perform(delete("/api/v1/members/99999"))  // 존재하지 않는 회원번호
                .andExpect(status().isBadRequest())       // 예외 처리에 따라 404 or 400
                .andExpect(jsonPath("$.code").value("MEMBER_NOT_FOUND"));
    }

    @Test
    @DisplayName("7. 비밀번호 변경 성공")
    void testUpdatePassword() throws Exception {
        MemberUpdatePasswordRequest passwordRequest = new MemberUpdatePasswordRequest(
                password, "newpass123", "newpass123"
        );

        mockMvc.perform(put("/api/v1/members/" + mbNo + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(passwordRequest)))
                .andExpect(status().isNoContent());
    }
}
