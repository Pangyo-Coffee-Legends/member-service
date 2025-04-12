package com.nhnacademy.memberservice.role.service;

import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import com.nhnacademy.memberservice.role.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * {@link RoleServiceImpl} 클래스에 대한 단위 테스트입니다.
 * 역할 등록, 조회, 수정 로직의 정상 작동 여부를 검증합니다.
 */
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role(1L, "USER", "일반 사용자");
    }

    @Test
    @DisplayName("1. 역할 등록 성공 테스트")
    void testRegisterRole_success() {
        // given
        RoleRegisterRequest request = new RoleRegisterRequest("USER", "일반 사용자");
        when(roleRepository.save(any(Role.class))).thenReturn(role);

        // when
        RoleResponse response = roleService.registerRole(request);

        // then
        assertThat(response.getRoleName()).isEqualTo("USER");
        assertThat(response.getRoleDescription()).isEqualTo("일반 사용자");
    }

    @Test
    @DisplayName("2. 역할 조회 성공 테스트")
    void testGetRole_success() {
        // given
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        // when
        RoleResponse response = roleService.getRole(1L);

        // then
        assertThat(response.getRoleName()).isEqualTo("USER");
        assertThat(response.getRoleDescription()).isEqualTo("일반 사용자");
    }

    @Test
    @DisplayName("3. 존재하지 않는 역할 조회 실패 테스트")
    void testGetRole_notFound() {
        // given
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> roleService.getRole(99L));
    }

    @Test
    @DisplayName("4. 역할 수정 성공 테스트")
    void testUpdateRole_success() {
        // given
        RoleUpdateRequest request = new RoleUpdateRequest(1L, "수정된 설명");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(roleRepository.save(any(Role.class))).thenReturn(new Role(1L, "USER", "수정된 설명"));

        // when
        RoleResponse response = roleService.updateRole(request);

        // then
        assertThat(response.getRoleDescription()).isEqualTo("수정된 설명");
    }

    @Test
    @DisplayName("5. 존재하지 않는 역할 수정 실패 테스트")
    void testUpdateRole_notFound() {
        // given
        RoleUpdateRequest request = new RoleUpdateRequest(999L, "설명 없음");
        when(roleRepository.findById(999L)).thenReturn(Optional.empty());

        // then
        assertThrows(IllegalArgumentException.class, () -> roleService.updateRole(request));
    }
}
