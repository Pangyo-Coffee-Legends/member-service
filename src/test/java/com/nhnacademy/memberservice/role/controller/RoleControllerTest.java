package com.nhnacademy.memberservice.role.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.exception.RoleConflictException;
import com.nhnacademy.memberservice.role.exception.RoleNotFoundException;
import com.nhnacademy.memberservice.role.service.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    RoleService roleService;

    @Test
    @DisplayName("권한 생성")
    void registerRole() throws Exception {
        RoleRegisterRequest request = new RoleRegisterRequest("ROLE_USER", "사용자");
        String body = mapper.writeValueAsString(request);
        RoleResponse response = new RoleResponse("ROLE_USER", "사용자");
        when(roleService.registerRole(request)).thenReturn(response);
        mockMvc.perform(
                post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"))
                .andExpect(jsonPath("$.roleDescription").value("사용자"))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 생성 실패 - 이름 중복")
    void registerRole_fail_conflict() throws Exception {
        RoleRegisterRequest request = new RoleRegisterRequest("ROLE_USER", "사용자");
        String body = mapper.writeValueAsString(request);
        doThrow(new RoleConflictException(request.getRoleName())).when(roleService).registerRole(request);
        mockMvc.perform(
                        post("/api/v1/roles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(body)
                ).andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    @DisplayName("권한 조회")
    void getRole() throws Exception {
        RoleResponse response = new RoleResponse("ROLE_USER", "사용자");
        when(roleService.getRole(1L)).thenReturn(response);
        mockMvc.perform(
                get("/api/v1/roles/{roleNo}", 1L)
                        .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("ROLE_USER"))
                .andExpect(jsonPath("$.roleDescription").value("사용자"))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 조회 실패")
    void getRole_fail_notfound() throws Exception {
        doThrow(new RoleNotFoundException("회원 번호를 찾을 수 없습니다.")).when(roleService).getRole(1L);
        mockMvc.perform(
                        get("/api/v1/roles/{roleNo}", 1L)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("권한 수정")
    void updateRole() throws Exception {
        RoleUpdateRequest request = new RoleUpdateRequest("ROLE_ADMIN", "관리자");
        String body = mapper.writeValueAsString(request);
        RoleResponse response = new RoleResponse("ROLE_ADMIN", "관리자");
        when(roleService.updateRole(1L, request)).thenReturn(response);

        mockMvc.perform(
                put("/api/v1/roles/{roleNo}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(body)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("ROLE_ADMIN"))
                .andExpect(jsonPath("$.roleDescription").value("관리자"))
                .andDo(print());
    }

    @Test
    @DisplayName("권한 수정 실패")
    void updateRole_fail_notfound() throws Exception {
        RoleUpdateRequest request = new RoleUpdateRequest("ROLE_ADMIN", "관리자");
        String body = mapper.writeValueAsString(request);
        doThrow(new RoleNotFoundException(request.getRoleName())).when(roleService).updateRole(1L, request);

        mockMvc.perform(
                        put("/api/v1/roles/{roleNo}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .content(body)
                )
                .andExpect(status().isNotFound())
                .andDo(print());
    }
}