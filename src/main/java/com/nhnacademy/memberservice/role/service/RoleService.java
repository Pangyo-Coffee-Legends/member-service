package com.nhnacademy.memberservice.role.service;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.role.dto.RoleRegisterRequest;
import com.nhnacademy.memberservice.role.dto.RoleResponse;
import com.nhnacademy.memberservice.role.dto.RoleUpdateRequest;

import java.util.List;

/**
 * 역할(Role)과 관련된 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * <p>
 * 이 인터페이스는 역할 등록, 조회, 수정 등의 기능을 제공합니다.
 * </p>
 */
public interface RoleService {

    /**
     * 새로운 역할을 등록합니다.
     * <p>
     * 역할 이름과 설명을 포함하는 요청 객체를 기반으로 역할을 생성합니다.
     * </p>
     *
     * @param request 역할 등록 요청을 담은 객체
     * @return 생성된 역할 정보를 담은 응답 객체
     */
    RoleResponse registerRole(RoleRegisterRequest request);

    /**
     * 역할 번호(roleNo)를 기준으로 역할 정보를 조회합니다.
     *
     * @param roleNo 조회할 역할의 고유 번호
     * @return 조회된 역할 정보를 담은 응답 객체
     */
    RoleResponse getRole(Long roleNo);

    /**
     * 역할 이름(roleName)를 기준으로 역할 정보를 조회합니다.
     *
     * @param roleName 조회할 역할 이름
     * @return 조회된 역할 정보를 담은 응답 객체
     */
    List<Member> getRoleList(String roleName);

    /**
     * 역할 정보를 수정합니다.
     * <p>
     * 주로 역할 설명(`roleDescription`)을 수정하는 데 사용됩니다.
     * 역할 이름(`roleName`)은 변경되지 않습니다.
     * </p>
     *
     * @param request 수정할 역할 정보를 담은 요청 객체
     * @return 수정된 역할 정보를 담은 응답 객체
     */
    RoleResponse updateRole(RoleUpdateRequest request);
}