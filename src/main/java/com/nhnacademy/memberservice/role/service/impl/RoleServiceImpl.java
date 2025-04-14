package com.nhnacademy.memberservice.role.service.impl;

import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import com.nhnacademy.memberservice.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 역할(Role)에 대한 비즈니스 로직을 처리하는 서비스 구현 클래스입니다.
 * <p>
 * 역할 등록, 조회, 수정 기능을 제공합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleResponse registerRole(RoleRegisterRequest request) {
        Role role = Role.ofNewRole(request.getRoleName(), request.getRoleDescription());
        Role saved = roleRepository.save(role);

        return new RoleResponse(
                saved.getRoleName(),
                saved.getRoleDescription()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRole(Long roleNo) {
        Role role = roleRepository.findById(roleNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 역할을 찾을 수 없습니다: roleNo=" + roleNo));

        return new RoleResponse(
                role.getRoleName(),
                role.getRoleDescription()
        );
    }

    @Override
    public RoleResponse updateRole(RoleUpdateRequest request) {
        Role role = roleRepository.findById(request.getRoleNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 역할을 찾을 수 없습니다: roleNo=" + request.getRoleNo()));

        role.update(role.getRoleName(), request.getRoleDescription());
        Role updated = roleRepository.save(role);

        return new RoleResponse(
                updated.getRoleName(),
                updated.getRoleDescription()
        );
    }
}
