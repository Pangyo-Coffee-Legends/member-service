package com.nhnacademy.memberservice.role.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("integration")
class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @Test
    @DisplayName("1. 역할 등록 성공")
    void testRegisterRole() throws Exception {
        RoleRegisterRequest request = new RoleRegisterRequest("INTEGRATION_TEST", "통합테스트용 권한");

        mockMvc.perform(post("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                 .andExpect(status().isCreated())
                .andExpect(jsonPath("$.roleName").value("INTEGRATION_TEST"))
                .andExpect(jsonPath("$.roleDescription").value("통합테스트용 권한"));

        Role saved = roleRepository.findByRoleName("INTEGRATION_TEST").orElseThrow();
        assertThat(saved.getRoleDescription()).isEqualTo("통합테스트용 권한");
    }

    @Test
    @DisplayName("2. 역할 조회 성공")
    void testGetRole() throws Exception {
        Role role = Role.ofNewRole("FETCH_TEST", "조회 테스트용");
        Role saved = roleRepository.save(role);

        mockMvc.perform(get("/api/v1/roles/id/" + saved.getRoleNo()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleName").value("FETCH_TEST"))
                .andExpect(jsonPath("$.roleDescription").value("조회 테스트용"));
    }

    @Test
    @DisplayName("3. 역할 설명 수정 성공")
    void testUpdateRole() throws Exception {
        Role role = Role.ofNewRole("UPDATE_TEST", "초기 설명");
        Role saved = roleRepository.save(role);

        RoleUpdateRequest request = new RoleUpdateRequest();
        request.setRoleNo(saved.getRoleNo());
        request.setRoleDescription("수정된 설명");

        mockMvc.perform(put("/api/v1/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.roleDescription").value("수정된 설명"));

        Role updated = roleRepository.findById(saved.getRoleNo()).orElseThrow();
        assertThat(updated.getRoleDescription()).isEqualTo("수정된 설명");
    }
}
