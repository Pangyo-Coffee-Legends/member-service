package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdatePasswordRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.domain.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    private ObjectMapper objectMapper;

    private Role mockRole;
    private MemberRegisterRequest registerRequest;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        mockRole = Role.ofNewRole("ROLE_USER", "일반 회원 권한");
        ReflectionTestUtils.setField(mockRole, "roleNo", 1L);


        registerRequest = new MemberRegisterRequest(
                "role",
                "김미성",
                "test@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1234-5678"
        );
    }

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
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andDo(print());

        verify(memberService).registerMember(any());
    }

    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMemberByEmail() throws Exception {
        MemberResponse response = new MemberResponse(
                1L,
               "role",
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

//    @Test
//    @DisplayName("3. 회원 수정 성공")
//    void testUpdateMember() throws Exception {
//        long mbNo = 1L;
////
////        MemberUpdateRequest updateRequest = new MemberUpdateRequest(
////                "role",
////                "김미성",
////                "update@example.com",
////                "newpassword",
////                "newpassword",
////                "010-0000-0000"
////        );
//
//        MemberResponse stubResponse = new MemberResponse(
//                1L,
//                "ROLE_USER",
//                "김미성",
//                "update@example.com",
//                "010-0000-0000"
//        );
//
//        // ✔ mbNo 기준 stub 등록
//        when(memberService.updateMember(eq(mbNo), any(MemberUpdateRequest.class)))
//                .thenReturn(stubResponse);
//
//        mockMvc.perform(put("/api/v1/members/" + mbNo)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(updateRequest)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.email").value("update@example.com"))
//                .andExpect(jsonPath("$.phoneNumber").value("010-0000-0000"));
//
//        verify(memberService).updateMember(eq(mbNo), any(MemberUpdateRequest.class));
//    }



    @Test
    @DisplayName("4. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/1"))
                .andExpect(status().isNoContent());

        verify(memberService).deleteMember(1L);
    }

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
}
