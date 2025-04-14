package com.nhnacademy.memberservice.role.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link RoleController}의 단위 테스트 클래스입니다.
 * 역할 등록, 조회, 수정 기능을 테스트합니다.
 */
@SpringBootTest
class RoleControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private RoleService roleService;

    @InjectMocks
    private RoleController roleController;

    private RoleRegisterRequest request;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(roleController).build();
        objectMapper= new ObjectMapper();

        request = new RoleRegisterRequest(
                "ADMIN",
                "관리자 권한"
        );



    }

    /**
     * 1. 역할 등록 성공 테스트
     */
    @Test
    @DisplayName("1. 역할 등록 성공")
    void testRegisterRole() throws Exception {
        RoleResponse response = new RoleResponse(
                request.getRoleName(),
                request.getRoleDescription()
        );


        when(roleService.registerRole(any())).thenReturn(response);

        mockMvc.perform(post("/roles/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("ADMIN"))
                .andExpect(jsonPath("$.roleDescription").value("관리자 권한"));
    }

    /**
     * 2. 역할 조회 성공 테스트
     */
    @Test
    @DisplayName("2. 역할 조회 성공")
    void testGetRole() throws Exception {
        RoleResponse response = new RoleResponse(
                request.getRoleName(),
                request.getRoleDescription()
        );


        when(roleService.getRole(1L)).thenReturn(response);

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("ADMIN"))
                .andExpect(jsonPath("$.roleDescription").value("관리자 권한"));
    }

    /**
     * 3. 역할 수정 성공 테스트
     */
    @Test
    @DisplayName("3. 역할 설명 수정 성공")
    void testUpdateRole() throws Exception {
        RoleResponse response = new RoleResponse(
                "USER",
                "request.getRoleDescription()"
        );
        when(roleService.updateRole(any())).thenReturn(response);

        mockMvc.perform(put("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(jsonPath("$.roleDescription").value("request.getRoleDescription()"));
    }
}
