package com.nhnacademy.memberservice.member.controller;

import com.nhnacademy.memberservice.member.dto.MemberRegisterRequest;
import com.nhnacademy.memberservice.member.dto.MemberResponse;
import com.nhnacademy.memberservice.member.dto.MemberUpdateRequest;
import com.nhnacademy.memberservice.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 회원(Member)에 대한 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 * <p>
 * 회원 등록, 조회, 수정, 탈퇴 기능을 RESTful API 형태로 제공합니다.
 * </p>
 */
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원을 등록합니다.
     *
     * @param request 회원 등록 요청 DTO
     * @return 등록된 회원의 정보
     */
    @PostMapping("/register")
    public ResponseEntity<MemberResponse> registerMember(@RequestBody @Valid MemberRegisterRequest request) {
        MemberResponse response = memberService.registerMember(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 정보를 조회합니다.
     *
     * @param mbNo 회원 고유 번호
     * @return 조회된 회원 정보
     */
    @GetMapping("/{mbNo}")
    public ResponseEntity<MemberResponse> getMember(@PathVariable Long mbNo) {
        MemberResponse response = memberService.getMember(mbNo);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원 정보를 수정합니다.
     *
     * @param request 회원 수정 요청 DTO
     * @return 수정된 회원 정보
     */
    @PutMapping
    public ResponseEntity<MemberResponse> updateMember(@RequestBody @Valid MemberUpdateRequest request) {
        MemberResponse response = memberService.updateMember(request);
        return ResponseEntity.ok(response);
    }

    /**
     * 회원을 탈퇴 처리합니다.
     * <p>
     * 실제 데이터 삭제가 아닌, soft-delete 방식으로 처리합니다.
     * </p>
     *
     * @param mbNo 탈퇴할 회원 고유 번호
     * @return 상태 코드 204 (No Content)
     */
    @DeleteMapping("/{mbNo}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long mbNo) {
        memberService.deleteMember(mbNo);
        return ResponseEntity.noContent().build();
    }
}
