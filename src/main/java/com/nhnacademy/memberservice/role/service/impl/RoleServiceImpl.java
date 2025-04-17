package com.nhnacademy.memberservice.role.service.impl;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.exception.RoleConflictException;
import com.nhnacademy.memberservice.role.exception.RoleNotFoundException;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import com.nhnacademy.memberservice.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        roleRepository.findByRoleName(request.getRoleName())
                .ifPresent(existingRole -> {
                    throw new RoleConflictException(request.getRoleName());
                });

        // 중복이 없으면 새로 Role 생성
        Role role = Role.ofNewRole(request.getRoleName(), request.getRoleDescription());

        // Role 저장
        Role saved = roleRepository.save(role);

        // 성공적으로 저장된 Role 정보 반환
        return new RoleResponse(
                saved.getRoleName(),
                saved.getRoleDescription()
        );
    }

    @Override
    public RoleResponse getRole(Long roleNo) {
        Role role = roleRepository.findById(roleNo)
                .orElseThrow(() -> new RoleNotFoundException("회원 번호를 찾을 수 없습니다."));

        return new RoleResponse(
                role.getRoleName(),
                role.getRoleDescription()
        );
    }

    @Override
    public RoleResponse updateRole(RoleUpdateRequest request) {
        Role role = roleRepository.findById(request.getRoleNo())
                .orElseThrow(() -> new RoleNotFoundException(request.getRoleName()));

        role.update(role.getRoleName(), request.getRoleDescription());
        Role updated = roleRepository.save(role);

        return new RoleResponse(
                updated.getRoleName(),
                updated.getRoleDescription()
        );
    }
}
