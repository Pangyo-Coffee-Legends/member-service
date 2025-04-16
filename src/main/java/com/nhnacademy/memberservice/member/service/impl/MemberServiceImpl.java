package com.nhnacademy.memberservice.member.service.impl;

import com.nhnacademy.memberservice.member.domain.Member;
import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdatePasswordRequest;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.exception.MemberNotFoundException;
import com.nhnacademy.memberservice.member.exception.NewPasswordNotMatchException;
import com.nhnacademy.memberservice.member.exception.PasswordNotMatchException;
import com.nhnacademy.memberservice.member.repository.MemberRepository;
import com.nhnacademy.memberservice.member.service.MemberService;
import com.nhnacademy.memberservice.role.domain.Role;
import com.nhnacademy.memberservice.role.exception.RoleNotFoundException;
import com.nhnacademy.memberservice.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
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
     * {@inheritDoc}
     *
     * @throws RoleNotFoundException 기본 역할(USER)을 찾을 수 없습니다.
     */
    @Override
    public MemberResponse registerMember(MemberRegisterRequest request) {
        Role role = Optional.ofNullable(request.getRole()).orElseGet(() -> roleRepository.findByRoleName("USER")
                .orElseThrow(() -> new RoleNotFoundException("USER")));

        Member member = Member.ofNewMember(request.getName(), request.getEmail(), request.getPassword(), request.getPhoneNumber());

        member.assignRole(role);

        Member saved = memberRepository.save(member);

        if (!request.isPasswordValid()) {
            throw new IllegalArgumentException("비밀번호와 비밀번호 확인이 일치하지 않습니다.");
        }

        return new MemberResponse(saved.getRole(), saved.getMbName(), saved.getMbEmail(), saved.getMbPassword(), saved.getPhoneNumber());

    }

    @Override
    public MemberResponse getMemberByMbNo(Long mbNo) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(() -> new MemberNotFoundException(mbNo + "는 존재하지 않는 회원입니다."));

        return new MemberResponse(member.getRole(), member.getMbName(), member.getMbEmail(), member.getMbPassword(), member.getPhoneNumber());
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 회원이 존재하지 않을 경우 예외 발생
     */
    @Override
    @Transactional(readOnly = true)
    public MemberResponse getMemberByEmail(String mbEmail) {
        Member member = memberRepository.findByMbEmail(mbEmail)
                .orElseThrow(() -> new MemberNotFoundException(mbEmail + "는 존재하지 않는 회원입니다."));

        return new MemberResponse(member.getRole(), member.getMbName(), member.getMbEmail(), member.getMbPassword(), member.getPhoneNumber());

    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException  회원이 존재하지 않음
     * @throws IllegalArgumentException 역할이 존재하지 않을 경우
     */
    @Override
    public MemberResponse updateMember(MemberUpdateRequest request) {
        Member member = memberRepository.findById(request.getMbNo())
                .orElseThrow(MemberNotFoundException::new);

        Role role = Optional.ofNullable(request.getRole())
                .orElseGet(() -> roleRepository.findByRoleName("USER")
                        .orElseThrow(() -> new RoleNotFoundException("기본 역할(USER)을 찾을 수 없습니다.")));

        member.assignRole(role);

        // 필드 직접 설정
        member = Member.ofNewMember(request.getName(), request.getEmail(), request.getPassword(), request.getPhoneNumber());
        member.assignRole(role);

        Member updated = memberRepository.save(member);

        return new MemberResponse(updated.getRole(), updated.getMbName(), updated.getMbEmail(), updated.getMbPassword(), updated.getPhoneNumber());
    }

    /**
     * {@inheritDoc}
     *
     * @throws MemberNotFoundException 회원이 존재하지 않는 경우 예외 발생
     */
    @Override
    public void deleteMember(Long mbNo) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(MemberNotFoundException::new);

        member.withdraw();
    }

    @Override
    public void updatePassword(Long mbNo, MemberUpdatePasswordRequest request) {
        Member member = memberRepository.findById(mbNo)
                .orElseThrow(MemberNotFoundException::new);

        if (!Objects.equals(member.getMbPassword(), request.getOldPassword())) {
            throw new PasswordNotMatchException();
        }

        if (!request.isPasswordValid()) {
            throw new NewPasswordNotMatchException();
        }

        member.updatePassword(request.getNewPassword());

    }

}
