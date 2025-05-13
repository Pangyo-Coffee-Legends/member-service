package com.nhnacademy.memberservice.member.service;

import com.nhnacademy.memberservice.member.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 회원 관련 서비스를 제공하는 인터페이스입니다.
 * 이 인터페이스는 회원의 등록, 조회, 수정, 삭제와 같은 기본적인 회원 관리 작업을 정의합니다.
 */
public interface MemberService {

    /**
     * 새 회원을 등록하는 메서드입니다.
     * <p>
     * 이 메서드는 회원의 정보를 포함하는 {@link MemberRegisterRequest} 객체를 받아서
     * 새 회원을 등록하고, 등록된 회원의 정보를 {@link MemberResponse} 형태로 반환합니다.
     * </p>
     *
     * @param request 회원 등록 요청을 담은 {@link MemberRegisterRequest} 객체
     * @return 등록된 회원의 정보가 담긴 {@link MemberResponse} 객체
     */
    MemberResponse registerMember(MemberRegisterRequest request);

    /**
     * 회원 정보를 조회하는 메서드입니다.
     * <p>
     * 이 메서드는 회원의 고유 ID인 {@code mbNo}를 받아 해당 회원의 정보를 조회하고
     * 조회된 회원의 정보를 {@link MemberResponse} 형태로 반환합니다.
     * </p>
     *
     * @param mbNo 조회할 회원의 고유 ID
     * @return 조회된 회원의 정보가 담긴 {@link MemberResponse} 객체
     */
    MemberResponse getMemberByMbNo(Long mbNo);

    /**
     * 회원 정보를 조회하는 메서드입니다.
     * <p>
     * 이 메서드는 회원의 고유 ID인 {@code mbNo}를 받아 해당 회원의 정보를 조회하고
     * 조회된 회원의 정보를 {@link MemberResponse} 형태로 반환합니다.
     * </p>
     *
     * @param mbEmail 조회할 회원의 고유 ID
     * @return 조회된 회원의 정보가 담긴 {@link MemberResponse} 객체
     */
    MemberResponse getMemberByEmail(String mbEmail);



    /**
     * 회원 정보를 수정하는 메서드입니다.
     * <p>
     * 이 메서드는 회원 수정 요청을 담고 있는 {@link MemberUpdateRequest} 객체를 받아
     * 해당 회원의 정보를 업데이트하고, 수정된 회원의 정보를 {@link MemberResponse} 형태로 반환합니다.
     * </p>
     *
     * @param request 수정된 회원 정보를 담은 {@link MemberUpdateRequest} 객체
     * @return 수정된 회원의 정보가 담긴 {@link MemberResponse} 객체
     */
    MemberResponse updateMember(Long mbNo, MemberUpdateRequest request);

    /**
     * 회원을 삭제하는 메서드입니다.
     * <p>
     * 이 메서드는 회원의 고유 ID인 {@code mbNo}를 받아 해당 회원을 삭제합니다.
     * 삭제된 회원은 시스템에서 더 이상 접근할 수 없습니다.
     * </p>
     *
     * @param mbNo 삭제할 회원의 고유 ID
     */
    void deleteMember(Long mbNo);

    /**
     * 지정된 회원 번호에 해당하는 사용자의 비밀번호를 수정합니다.
     *
     * @param mbNo 비밀번호를 수정할 대상 회원의 고유 번호 (Primary Key)
     * @param request 비밀번호 수정 요청 정보를 담은 DTO 객체.
     *                현재 비밀번호, 새로운 비밀번호, 비밀번호 확인 등의 정보가 포함됩니다.
     */
    void updatePassword(Long mbNo, MemberUpdatePasswordRequest request);

    Page<MemberInfoResponse> getMemberInfoList(Pageable pageable);

    List<MemberNoResponse> getAllMemberIds();
}
