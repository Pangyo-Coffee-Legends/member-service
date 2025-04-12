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
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * 역할을 등록합니다.
     *
     * @param request 등록할 역할 정보 (이름, 설명 포함)
     * @return 등록된 역할의 응답 DTO
     */
    @Override
    @Transactional
    public RoleResponse registerRole(RoleRegisterRequest request) {
        Role role = new Role(null, request.getRoleName(), request.getRoleDescription());
        Role saved = roleRepository.save(role);

        RoleResponse response = new RoleResponse();
        response.setRoleName(saved.getRoleName());
        response.setRoleDescription(saved.getRoleDescription());
        return response;
    }

    /**
     * 주어진 역할 번호에 해당하는 역할 정보를 조회합니다.
     *
     * @param roleNo 조회할 역할의 고유 번호
     * @return 조회된 역할의 응답 DTO
     * @throws IllegalArgumentException 역할이 존재하지 않을 경우 예외 발생
     */
    @Override
    @Transactional(readOnly = true)
    public RoleResponse getRole(Long roleNo) {
        Role role = roleRepository.findById(roleNo)
                .orElseThrow(() -> new IllegalArgumentException("해당 역할을 찾을 수 없습니다: roleNo=" + roleNo));

        RoleResponse response = new RoleResponse();
        response.setRoleName(role.getRoleName());
        response.setRoleDescription(role.getRoleDescription());
        return response;
    }

    /**
     * 주어진 정보로 역할을 수정합니다.
     *
     * @param request 수정할 역할 정보 (roleNo, roleDescription)
     * @return 수정된 역할의 응답 DTO
     * @throws IllegalArgumentException 역할이 존재하지 않을 경우 예외 발생
     */
    @Override
    @Transactional
    public RoleResponse updateRole(RoleUpdateRequest request) {
        Role role = roleRepository.findById(request.getRoleNo())
                .orElseThrow(() -> new IllegalArgumentException("해당 역할을 찾을 수 없습니다: roleNo=" + request.getRoleNo()));

        role.update(role.getRoleName(), request.getRoleDescription());
        Role updated = roleRepository.save(role);

        RoleResponse response = new RoleResponse();
        response.setRoleName(updated.getRoleName());
        response.setRoleDescription(updated.getRoleDescription());
        return response;
    }
}
