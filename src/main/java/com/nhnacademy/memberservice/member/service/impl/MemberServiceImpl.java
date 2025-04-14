package com.nhnacademy.memberservice.member.service.impl;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.exception.MemberNotFoundException;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.exception.RoleNotFoundException;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 회원 관련 비즈니스 로직을 담당하는 구현 클래스입니다.
 * <p>
 * 회원 등록, 조회, 수정, 삭제 기능을 제공합니다.
 * 모든 메서드는 트랜잭션을 고려하여 설계되어 데이터 정합성을 보장합니다.
 * </p>
 */
@Service
@RequiredArgsConstructor
@Transactional
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    /**
     * 새로운 회원을 등록합니다.
     * <p>
     * 회원가입 요청 정보를 받아 {@link Member} 엔티티로 변환 후 저장합니다.
     * </p>
     *
     * @param request 회원 가입 요청 정보
     * @return 등록된 회원 정보
     */
    @Override
    public MemberResponse registerMember(MemberRegisterRequest request) {
        Role role = Optional.ofNullable(request.getRole()).orElseGet(() -> roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RoleNotFoundException("기본 역할(USER)을 찾을 수 없습니다.")));

        Member member = Member.ofNewMember(request.getName(), request.getEmail(), request.getPassword(), request.getPhoneNumber());

        member.assignRole(role);

        Member saved = memberRepository.save(member);

        return new MemberResponse(saved.getRole(), saved.getMbName(), saved.getMbEmail(), saved.getPhoneNumber());
    }

    /**
     * 회원 고유 ID로 회원 정보를 조회합니다.
     *
     * @param mbNo 회원 고유 번호
     * @return 해당 회원의 정보
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우 예외 발생
     */
    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMember(Long mbNo) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException(mbNo + "는 존재하지 않는 회원입니다."));

        return new MemberResponse(member.getRole(), member.getMbName(), member.getMbEmail(), member.getPhoneNumber());
    }

    /**
     * 회원 정보를 수정합니다.
     *
     * @param request 수정 요청 DTO
     * @return 수정된 회원 정보
     * @throws MemberNotFoundException  회원이 존재하지 않음
     * @throws IllegalArgumentException 역할이 존재하지 않을 경우
     */
    @Override
    public MemberResponse updateMember(MemberUpdateRequest request) {
        Member member = memberRepository.findById(request.getMbNo())
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        Role role = Optional.ofNullable(request.getRole())
                .orElseGet(() -> roleRepository.findByRoleName("USER")
                        .orElseThrow(() -> new MemberNotFoundException("기본 역할(USER)을 찾을 수 없습니다.")));

        member.assignRole(role);

        // 필드 직접 설정
        member = Member.ofNewMember(request.getName(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        member.assignRole(role);

        Member updated = memberRepository.save(member);

        return new MemberResponse(updated.getRole(), updated.getMbName(), updated.getMbEmail(), updated.getPhoneNumber());
    }

    /**
     * 회원을 탈퇴 처리(소프트 딜리트)합니다.
     * <p>
     * 실제로 삭제하지 않고, {@code withdrawnAt} 필드에 시각을 기록합니다.
     * </p>
     *
     * @param mbNo 회원 고유 번호
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우 예외 발생
     */
    @Override
    public void deleteMember(Long mbNo) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

        member.withdraw();
    }
}
