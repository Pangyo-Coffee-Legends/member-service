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
 * 이 클래스는 {@link RoleService}를 구현하며, 역할 등록, 조회, 수정과 같은 기능을 제공합니다.
 * 역할 중복 체크, 예외 처리, 트랜잭션 처리 등을 통해 데이터 정합성을 보장합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    /**
     * 새로운 역할(Role)을 등록합니다.
     *
     * @param request 역할 등록 요청 DTO (역할 이름 및 설명 포함)
     * @return 등록된 역할 정보를 담은 응답 DTO
     * @throws RoleConflictException 동일한 이름의 역할이 이미 존재하는 경우
     */
    @Override
    public RoleResponse registerRole(RoleRegisterRequest request) {
        roleRepository.findByRoleName(request.getRoleName())
                .ifPresent(existingRole -> {
                    throw new RoleConflictException(request.getRoleName());
                });

        Role role = Role.ofNewRole(request.getRoleName(), request.getRoleDescription());
        Role saved = roleRepository.save(role);

        return new RoleResponse(
                saved.getRoleName(),
                saved.getRoleDescription()
        );
    }

    /**
     * 역할 번호(roleNo)를 기준으로 역할 정보를 조회합니다.
     *
     * @param roleNo 조회할 역할의 고유 번호
     * @return 조회된 역할 정보를 담은 응답 DTO
     * @throws RoleNotFoundException 해당 번호의 역할이 존재하지 않는 경우
     */
    @Override
    public RoleResponse getRole(Long roleNo) {
        Role role = roleRepository.findById(roleNo)
                .orElseThrow(() -> new RoleNotFoundException("회원 번호를 찾을 수 없습니다."));

        return new RoleResponse(
                role.getRoleName(),
                role.getRoleDescription()
        );
    }

    /**
     * 역할 정보를 수정합니다.
     * <p>
     * 역할 이름은 수정하지 않고, 역할 설명만 변경됩니다.
     * </p>
     *
     * @param request 역할 수정 요청 DTO (roleNo, roleDescription 포함)
     * @return 수정된 역할 정보를 담은 응답 DTO
     * @throws RoleNotFoundException 요청한 roleNo가 존재하지 않는 경우
     */
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

