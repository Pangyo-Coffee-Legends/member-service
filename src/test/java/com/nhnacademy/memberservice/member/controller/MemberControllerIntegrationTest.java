package com.nhnacademy.memberservice.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@code MemberControllerIntegrationTest}는 {@link com.nhnacademy.memberservice.member.controller.MemberController}
 * 클래스에 대한 통합 테스트를 수행하는 클래스입니다.
 * <p>
 * {@code MockMvc}를 이용하여 실제 HTTP 요청/응답 흐름을 테스트하며,
 * 회원 등록, 조회, 수정, 탈퇴 기능의 전체 API 동작을 검증합니다.
 * </p>
 * <p>
 * Spring Boot의 전체 컨텍스트를 로딩하여 Service, Repository를 포함한 종단 간 테스트를 수행합니다.
 * </p>
 */
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

    private  MemberRegisterRequest request;
    private Long mbNo;
    private String email = "test@example123.com";

    /**
     * 테스트 실행 전, 회원 등록을 통해 테스트 데이터를 세팅합니다.
     */
    @BeforeEach
    void setUp() {
    request = new MemberRegisterRequest(
                "USER_MEMBER",
                "김미성",
                email,
                "password",
                "010-1234-5678",
                "password"
        );
        mbNo = memberService.registerMember(request).getMbNo();
    }


    @Test
    @DisplayName("1. 회원 등록 성공")
    void testRegisterMember() throws Exception {


    }
    /**
     * 이메일을 기준으로 회원 조회 요청을 수행하여,
     * 200 OK 응답과 함께 올바른 회원 정보를 반환하는지 검증합니다.
     */
    @Test
    @DisplayName("2. 회원 조회 성공")
    void testGetMemberByEmail() throws Exception {
        mockMvc.perform(get("/api/v1/members/email/" + email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mbName").value("김미성"))
                .andExpect(jsonPath("$.mbEmail").value(email));
    }

    /**
     * 회원 수정 요청을 통해 이메일과 전화번호를 변경하고,
     * 수정된 결과가 정상적으로 반영되는지 검증합니다.
     */
    @Test
    @DisplayName("3. 회원 수정 성공")
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

    /**
     * 회원 탈퇴 요청을 수행하고, 204 No Content 응답이 정상적으로 반환되는지 확인합니다.
     */
    @Test
    @DisplayName("4. 회원 탈퇴 성공")
    void testDeleteMember() throws Exception {
        mockMvc.perform(delete("/api/v1/members/" + mbNo))
                .andExpect(status().isNoContent());
    }


}
