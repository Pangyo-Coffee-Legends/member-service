package com.nhnacademy.memberservice.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.member.dto.*;
import com.nhnacademy.memberservice.member.service.MemberService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MemberService memberService;

    MemberResponse savedMember;
    @BeforeEach
    void setUp() {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                "ROLE_USER",
                "김미성",
                "test1@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1234-5678"
        );

        savedMember = memberService.registerMember(registerRequest);

    }


    @Test
    @Order(1)
    @DisplayName("회원 등록")
    void registerMember() throws Exception {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                "ROLE_USER",
                "김미성",
                "test@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1234-5678"
        );
        String body = mapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andDo(print());

    }

    @Test
    @Order(2)
    @DisplayName("회원 등록 실패 - bad request")
    void registerMember_fail_badrequest() throws Exception {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                "ROLE_NAME",
                "김미성",
                "test@example.com",
                "123As",
                "123As",
                "010-1234-567"
        );
        String body = mapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(3)
    @DisplayName("회원 등록 실패 - 권한 찾을 수 없음")
    void registerMember_fail_notfound() throws Exception {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                "ROLE_NAME",
                "김미성",
                "test@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1234-5678"
        );
        String body = mapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(4)
    @DisplayName("회원 등록 실패 - 이미 있는 이메일")
    void registerMember_fail_already() throws Exception {
        MemberRegisterRequest registerRequest = new MemberRegisterRequest(
                "ROLE_USER",
                "김미성",
                "test1@example.com",
                "123Asd!@#",
                "123Asd!@#",
                "010-1234-5678"
        );
        String body = mapper.writeValueAsString(registerRequest);

        mockMvc.perform(post("/api/v1/members")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @Order(5)
    @DisplayName("회원 조회")
    void getMember() throws Exception {
        mockMvc.perform(
                get("/api/v1/members/{mbNo}?view=detailed", savedMember.getNo())
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test1@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andDo(print());
    }

    @Test
    @Order(6)
    @DisplayName("회원 조회 실패 - not found")
    void getMember_fail_notfound() throws Exception {
        mockMvc.perform(
                        get("/api/v1/members/{mbNo}?view=detailed", 6L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(7)
    @DisplayName("회원 간단 정보 조회")
    void getMember_summary() throws Exception {
        mockMvc.perform(
                        get("/api/v1/members/{mbNo}?view=summary", savedMember.getNo())
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test1@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andDo(print());
    }

    @Test
    @Order(8)
    @DisplayName("회원 간단 정보 조회 실패")
    void getMember_summary_fail_notfound() throws Exception {
        mockMvc.perform(
                        get("/api/v1/members/{mbNo}?view=summary", 8L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(9)
    @DisplayName("회원 정보 조회 - 이메일")
    void getMemberByEmail() throws Exception {
        mockMvc.perform(
                        get("/api/v1/members/email/{mbEmail}?view=detailed", "test1@example.com")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("김미성"))
                .andExpect(jsonPath("$.email").value("test1@example.com"))
                .andExpect(jsonPath("$.phoneNumber").value("010-1234-5678"))
                .andDo(print());
    }

    @Test
    @Order(10)
    @DisplayName("회원 정보 조회 실패 - 이메일")
    void getMemberByEmail_fail_notfound() throws Exception {
        mockMvc.perform(
                        get("/api/v1/members/email/{mbEmail}?view=detailed", "test@example.com")
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(11)
    @DisplayName("회원 정보 수정")
    void updateMember() throws Exception {
        MemberUpdateRequest request = new MemberUpdateRequest("김미성", "test@example.com", "010-1234-5678");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                put("/api/v1/members/{mbNo}", savedMember.getNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(12)
    @DisplayName("회원 정보 수정 실패 - bad request")
    void updateMember_fail_badrequest() throws Exception {
        MemberUpdateRequest request = new MemberUpdateRequest("김미성", "test1", "010-1234-5678");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/v1/members/{mbNo}", savedMember.getNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(13)
    @DisplayName("회원 정보 수정 실패 - not found")
    void updateMember_fail_notfound() throws Exception {
        MemberUpdateRequest request = new MemberUpdateRequest("김미성", "test1@example.com", "010-1234-5678");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/v1/members/{mbNo}", 13L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(14)
    @DisplayName("회원 탈퇴")
    void deleteMember() throws Exception {
        mockMvc.perform(
                delete("/api/v1/members/{mbNo}", savedMember.getNo())
        )
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Order(15)
    @DisplayName("회원 탈퇴 실패 - notfound")
    void deleteMember_fail_notfound() throws Exception {
        mockMvc.perform(
                        delete("/api/v1/members/{mbNo}", 15L)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @Order(16)
    @DisplayName("비밀번호 수정")
    void updatePassword() throws Exception {
        MemberUpdatePasswordRequest request = new MemberUpdatePasswordRequest("123Asd!@#", "Test123!", "Test123!");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                put("/api/v1/members/{mbNo}/password", savedMember.getNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    @Order(17)
    @DisplayName("비밀번호 수정 실패 - 기존 비밀번호 일치")
    void updatePassword_fail_notmatch_old() throws Exception {
        MemberUpdatePasswordRequest request = new MemberUpdatePasswordRequest("123Asd!@##", "Test123!", "Test123!");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/v1/members/{mbNo}/password", savedMember.getNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(18)
    @DisplayName("비밀번호 수정 실패 - 새 비밀번호 일치")
    void updatePassword_fail_notmatch_new() throws Exception {
        MemberUpdatePasswordRequest request = new MemberUpdatePasswordRequest("123Asd!@#", "Test123!", "Test123!!");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                        put("/api/v1/members/{mbNo}/password", savedMember.getNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    @Order(19)
    @DisplayName("회원 정보 리스트 조회")
    void getMemberInfoList() throws Exception {
        mockMvc.perform(
                get("/api/v1/members?page=1")
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(20)
    @DisplayName("회원 번호 리스트 조회")
    void getAllMemberIds() throws Exception {
        mockMvc.perform(
                        get("/api/v1/members/ids")
                                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Order(21)
    @DisplayName("비밀번호 인증 - 일치")
    void verify() throws Exception {
        MemberConfirmPasswordRequest request = new MemberConfirmPasswordRequest("123Asd!@#");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                post("/api/v1/members/{mbNo}/password", savedMember.getNo())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true))
                .andDo(print());
    }

    @Test
    @Order(22)
    @DisplayName("비밀번호 인증 - 불일치")
    void verify_fail() throws Exception {
        MemberConfirmPasswordRequest request = new MemberConfirmPasswordRequest("123Asd!@##");
        String body = mapper.writeValueAsString(request);

        mockMvc.perform(
                        post("/api/v1/members/{mbNo}/password", savedMember.getNo())
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false))
                .andDo(print());
    }
}
