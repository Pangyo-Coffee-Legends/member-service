package com.nhnacademy.memberservice.role.controller;

import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;
import com.nhnacademy.memberservice.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * 역할(Role) 관련 요청을 처리하는 REST 컨트롤러입니다.
 * <p>
 * 역할 등록, 조회, 수정 기능을 제공합니다.
 * RESTful API 표준에 맞게 URI 및 HTTP 메서드가 설계되어 있습니다.
 * </p>
 */
@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    /**
     * 새로운 역할을 등록합니다.
     *
     * @param request 등록할 역할의 이름과 설명이 담긴 요청 객체
     *                (예: 역할 이름: "ADMIN", 설명: "관리자 권한")
     * @return 등록된 역할 정보를 포함한 응답 객체
     *         (예: 역할 이름: "ADMIN", 설명: "관리자 권한")
     */
    @PostMapping
    public ResponseEntity<RoleResponse> registerRole(@Valid @RequestBody RoleRegisterRequest request) {
        RoleResponse role = roleService.registerRole(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    /**
     * 역할 고유 번호(roleNo)를 기준으로 역할 정보를 조회합니다.
     *
     * @param roleNo 조회할 역할의 고유 번호 (예: 1)
     * @return 해당 역할의 정보가 담긴 응답 객체
     *         (예: 역할 이름: "USER", 설명: "일반 사용자")
     */
    @GetMapping("/{roleNo}")
    public ResponseEntity<RoleResponse> getRole(@PathVariable("roleNo") Long roleNo) {
        RoleResponse role = roleService.getRole(roleNo);
        return ResponseEntity.ok(role);
    }


    /**
     * 역할 정보를 수정합니다.
     * <p>
     * 역할 이름은 수정되지 않으며, 설명만 변경됩니다.
     * </p>
     *
     * @param roleNo 역할 번호
     * @param request 수정할 역할 설명이 포함된 요청 객체
     *                (예: roleNo: 1, roleDescription: "시스템 관리자")
     * @return 수정된 역할 정보 응답 객체
     *         (예: 역할 이름: "ADMIN", 설명: "시스템 관리자")
     */
    @PatchMapping("/{roleNo}")
    public ResponseEntity<RoleResponse> updateRole(@PathVariable("roleNo") Long roleNo, @Validated @RequestBody RoleUpdateRequest request) {
        RoleResponse role = roleService.updateRole(roleNo, request);
        return ResponseEntity.ok(role);
    }
}
