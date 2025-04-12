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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        Mockito.reset(memberService);
    }

    @Test
    @DisplayName("1. 회원 등록 성공")
    void testRegisterMember() throws Exception {
        MemberRegisterRequest request = new MemberRegisterRequest(
                new Role(1L, "USER", "일반 사용자"),
                "김미성",
                "test@example.com",
                "password",
                "010-1234-5678",
                "password"
        );

        MemberResponse response = new MemberResponse(
                request.getRole(),
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );

        Mockito.when(memberService.registerMember(any())).thenReturn(response);

        mockMvc.perform(post("/members/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"));
    }

    // 다른 테스트들도 동일 패턴 적용 가능

    /**
     * 테스트 전용 MemberService Mock 등록
     */
    @TestConfiguration
    static class MockServiceConfig {
        @Bean
        @Primary
        public MemberService memberService() {
            return Mockito.mock(MemberService.class);
        }
    }

    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMember() throws Exception {
        MemberResponse response = new MemberResponse(
                new Role(1L, "USER", "일반 사용자"),
                "김미성",
                "test@example.com",
                "010-1234-5678"
        );

        Mockito.when(memberService.getMember(1L)).thenReturn(response);

        mockMvc.perform(get("/members/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @DisplayName("3. 회원 수정 성공")
    void testUpdateMember() throws Exception {
        MemberUpdateRequest request = new MemberUpdateRequest(
                1L,
                new Role(1L, "USER", "일반 사용자"),
                "김미성",
                "update@example.com",
                "newpassword",
                "newpassword",
                "010-0000-0000"
        );

        MemberResponse response = new MemberResponse(
                request.getRole(),
                request.getName(),
                request.getEmail(),
                request.getPhoneNumber()
        );

        Mockito.when(memberService.updateMember(any())).thenReturn(response);

        mockMvc.perform(put("/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("update@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-0000-0000"));
    }

    @Test
    @DisplayName("4. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/members/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(memberService).deleteMember(1L);
    }
}
