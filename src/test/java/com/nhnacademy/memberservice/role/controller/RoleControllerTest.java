package com.nhnacademy.memberservice.role.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * {@link RoleController}의 단위 테스트 클래스입니다.
 * 역할 등록, 조회, 수정 기능을 테스트합니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleService roleService;

    /**
     * 1. 역할 등록 성공 테스트
     */
    @Test
    @DisplayName("1. 역할 등록 성공")
    void testRegisterRole() throws Exception {
        RoleRegisterRequest request = new RoleRegisterRequest("ADMIN", "관리자 권한");
        RoleResponse response = new RoleResponse();
        response.setRoleName("ADMIN");
        response.setRoleDescription("관리자 권한");

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
        RoleResponse response = new RoleResponse();
        response.setRoleName("USER");
        response.setRoleDescription("일반 사용자");

        when(roleService.getRole(1L)).thenReturn(response);

        mockMvc.perform(get("/roles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(jsonPath("$.roleDescription").value("일반 사용자"));
    }

    /**
     * 3. 역할 수정 성공 테스트
     */
    @Test
    @DisplayName("3. 역할 설명 수정 성공")
    void testUpdateRole() throws Exception {
        RoleUpdateRequest request = new RoleUpdateRequest(1L, "수정된 설명");
        RoleResponse response = new RoleResponse();
        response.setRoleName("USER");
        response.setRoleDescription("수정된 설명");

        when(roleService.updateRole(any())).thenReturn(response);

        mockMvc.perform(put("/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("USER"))
                .andExpect(jsonPath("$.roleDescription").value("수정된 설명"));
    }
}
