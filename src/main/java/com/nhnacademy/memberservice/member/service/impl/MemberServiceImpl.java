package com.nhnacademy.memberservice.member.service.impl;

import com.nhnacademy.memberservice.member.entity.Member;
import com.nhnacademy.memberservice.member.dto.*;
import com.nhnacademy.memberservice.member.exception.*;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.exception.RoleNotFoundException;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 회원(Member) 관련 비즈니스 로직을 구현한 서비스 클래스입니다.
 * <p>
 * 회원 등록, 조회, 수정, 탈퇴, 비밀번호 변경 등 주요 기능을 제공합니다.
 * 각 기능은 데이터 정합성을 고려하여 트랜잭션을 적용하였으며,
 * 예외 상황에 따른 도메인별 커스텀 예외 처리를 포함하고 있습니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 신규 회원을 등록합니다.
     *
     * @param request 회원 등록 요청 DTO
     * @return 등록된 회원 정보를 담은 응답 DTO
     * @throws PasswordNotMatchException 비밀번호와 비밀번호 확인이 일치하지 않을 경우
     * @throws RoleNotFoundException 요청한 권한명이 존재하지 않을 경우
     * @throws MemberAlreadyExistsException 이미 해당 이메일로 등록된 회원이 존재할 경우
     */
    @Override
    public MemberResponse registerMember(MemberRegisterRequest request) {
        if (!request.isPasswordValid()) {
            throw new PasswordNotMatchException();
        }

        Role role = roleRepository.findByRoleName(request.getRoleName())
                .orElseThrow(() -> new RoleNotFoundException(request.getRoleName()));

        if (memberRepository.existsMemberByMbEmail(request.getEmail())) {
            throw new MemberAlreadyExistsException(request.getEmail());
        }

        Member member = Member.ofNewMember(
                role,
                request.getName(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getPhoneNumber()
        );

        Member savedMember = memberRepository.save(member);

        return getMemberResponse(savedMember);
    }

    /**
     * 회원 번호로 회원 정보를 조회합니다.
     *
     * @param mbNo 조회할 회원 고유 번호
     * @return 회원 정보 응답 DTO
     * @throws MemberNotFoundException 해당 회원 번호로 등록된 회원이 존재하지 않는 경우
     */
    @Override
    public MemberResponse getMemberByMbNo(Long mbNo) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException(mbNo));

        return getMemberResponse(member);
    }

    /**
     * 이메일로 회원 정보를 조회합니다.
     *
     * @param mbEmail 회원 이메일
     * @return 회원 정보 응답 DTO
     * @throws MemberEmailNotFoundException 해당 이메일로 등록된 회원이 없는 경우
     */
    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberByEmail(String mbEmail) {
        Member member = memberRepository.findByMbEmail(mbEmail)
                .orElseThrow(() -> new MemberEmailNotFoundException(mbEmail));

        return getMemberResponse(member);
    }

    /**
     * 회원의 이름, 전화번호 등 정보를 수정합니다.
     * <p>
     * 이메일, 비밀번호, 권한 등은 수정 대상이 아닙니다.
     * </p>
     *
     * @param mbNo 수정할 회원 번호
     * @param request 수정 요청 DTO
     * @return 수정된 회원 정보 응답 DTO
     * @throws MemberNotFoundException 해당 회원 번호로 등록된 회원이 존재하지 않는 경우
     */
    @Override
    public MemberResponse updateMember(Long mbNo, MemberUpdateRequest request) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException(mbNo));

        member.update(
                request.getName(),
                request.getPhoneNumber()
        );

        return getMemberResponse(member);
    }

    /**
     * 회원 탈퇴(Soft Delete)를 수행합니다.
     * <p>
     * 실제 DB 삭제는 수행하지 않으며, 탈퇴 일시 필드를 갱신하여 탈퇴 상태로 처리합니다.
     * </p>
     *
     * @param mbNo 탈퇴할 회원 번호
     * @throws MemberNotFoundException 해당 회원이 존재하지 않을 경우
     */
    @Override
    public void deleteMember(Long mbNo) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException(mbNo));

        member.withdraw();
    }

    /**
     * 회원 비밀번호를 변경합니다.
     *
     * @param mbNo 대상 회원 번호
     * @param request 비밀번호 변경 요청 DTO
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우
     * @throws PasswordNotMatchException 기존 비밀번호가 일치하지 않을 경우
     * @throws NewPasswordNotMatchException 새 비밀번호와 재확인 값이 일치하지 않을 경우
     */
    @Override
    public void updatePassword(Long mbNo, MemberUpdatePasswordRequest request) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException(mbNo));

        if (!Objects.equals(passwordEncoder.encode(member.getMbPassword()), passwordEncoder.encode(request.getOldPassword()))) {
            throw new PasswordNotMatchException();
        }

        if (!request.isPasswordValid()) {
            throw new NewPasswordNotMatchException();
        }

        member.updatePassword(passwordEncoder.encode(request.getNewPassword()));
    }

    /**
     * 회원 도메인 객체를 응답 DTO로 변환합니다.
     *
     * @param member 변환할 회원 객체
     * @return 응답 DTO
     */
    private MemberResponse getMemberResponse(Member member) {
        return new MemberResponse(
                member.getMbNo(),
                member.getRole().getRoleName(),
                member.getMbName(),
                member.getMbEmail(),
                member.getMbPassword(),
                member.getPhoneNumber()
        );
    }

    /**
     * 전체 회원의 요약 정보를 조회합니다.
     *
     * @return 회원 요약 정보 리스트
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우
     */
    @Override
    public List<MemberInfoResponse> getMemberInfoList() {
      return memberRepository.findAllMemberInfo();
    }
}