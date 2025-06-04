package com.nhnacademy.memberservice.member.controller;

import com.nhnacademy.memberservice.member.dto.*;
import com.nhnacademy.memberservice.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//
/**
 * 회원(Member)에 대한 HTTP 요청을 처리하는 컨트롤러 클래스입니다.
 * <p>
 * 회원 등록, 조회, 수정, 탈퇴 기능을 RESTful API 형태로 제공합니다.
 * URI 및 HTTP 메서드 규약에 따라 설계되어 있으며, 클라이언트-서버 간 명확한 역할 구분을 지원합니다.
 * </p>
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    /**
     * 새로운 회원을 등록합니다.
     *
     * @param request 회원 등록 요청 정보를 담은 DTO
     * @return 등록된 회원의 상세 정보가 담긴 ResponseEntity (HTTP 201 Created)
     */
    @PostMapping
    public ResponseEntity<MemberResponse> registerMember(@RequestBody @Valid MemberRegisterRequest request) {
        log.debug("Request from front-service has arrived! {}", request);
        MemberResponse response = memberService.registerMember(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * 회원 고유 번호로 특정 회원의 정보를 조회합니다.
     *
     * @param mbNo 조회할 회원의 고유 번호 (PathVariable)
     * @return 해당 회원의 정보가 담긴 ResponseEntity (HTTP 200 OK)
     */
    @GetMapping("/{mbNo}")
    public ResponseEntity<?> getMember(@PathVariable Long mbNo, @RequestParam(defaultValue = "detailed") String view) {

        MemberViewType viewType = parseViewType(view);

        return switch (viewType) {
            case SUMMARY -> {
                MemberInfoResponse summary = memberService.getMemberInfo(mbNo);
                yield ResponseEntity.ok(summary);
            }
            case DETAILED -> {
                MemberResponse detailed = memberService.getMemberByMbNo(mbNo);
                yield ResponseEntity.ok(detailed);
            }
        };
    }

    /**
     * 회원 고유 이메일로 특정 회원의 정보를 조회합니다.
     *
     * @param mbEmail 조회할 회원의 고유 번호 (PathVariable)
     * @return 해당 회원의 정보가 담긴 ResponseEntity (HTTP 200 OK)
     */
    @GetMapping("/email/{mbEmail}")
    public ResponseEntity<?> getMemberByEmail(@PathVariable String mbEmail, @RequestParam(defaultValue = "detailed") String view) {
        log.trace("조회 시작 : {}", view);
        MemberViewType viewType = parseViewType(view);

        log.trace("viewType: {}, service: {}", viewType, memberService);
        return switch (viewType) {
                case SUMMARY -> {
                    MemberInfoResponse summary = memberService.getMemberInfoByEmail(mbEmail);
                    yield ResponseEntity.ok(summary);
                }
                case DETAILED -> {
                    log.trace("view detailed 들어옴");
                    MemberResponse detailed = memberService.getMemberByEmail(mbEmail);
                    yield ResponseEntity.ok(detailed);
                }
            };
    }

    /**
     * 주어진 문자열을 {@link MemberViewType} enum 타입으로 변환합니다.
     *
     * <p>입력된 문자열이 "summary" 또는 "detailed"와 일치하지 않을 경우
     * {@link IllegalArgumentException}을 발생시키며, 메시지에는 허용되는 값에 대한 안내가 포함됩니다.</p>
     *
     * @param view 변환할 문자열 (예: "summary", "detailed")
     * @return 변환된 {@link MemberViewType} enum 값
     * @throws IllegalArgumentException view 값이 유효하지 않을 경우 발생
     */
    private MemberViewType parseViewType(String view) {
        try {
            return MemberViewType.from(view);
        } catch (IllegalArgumentException e) {
            log.trace("error : {}", e.getStackTrace());
            throw new IllegalArgumentException("view 파라미터 값이 올바르지 않습니다. 'summary' 또는 'detailed' 중 하나를 사용해 주세요.");
        }
    }

    /**
     * 회원 정보를 수정합니다.
     * <p>
     * 회원 이름, 이메일, 비밀번호, 전화번호 등의 정보를 업데이트합니다.
     * </p>
     *
     * @param request 수정할 회원 정보를 담은 DTO
     * @return 수정된 회원 정보가 담긴 ResponseEntity (HTTP 200 OK)
     */
    @PutMapping("/{mbNo}")
    public ResponseEntity<MemberResponse> updateMember(@PathVariable Long mbNo,@RequestBody @Valid MemberUpdateRequest request) {
        MemberResponse response = memberService.updateMember(mbNo,request);

        return ResponseEntity.ok(response);
    }

    /**
     * 회원을 탈퇴(소프트 삭제) 처리합니다.
     * <p>
     * 실제 데이터 삭제는 아니며, withdrawnAt 필드를 통해 탈퇴 상태를 표시합니다.
     * </p>
     *
     * @param mbNo 탈퇴할 회원의 고유 번호 (PathVariable)
     * @return 내용 없는 응답 ResponseEntity (HTTP 204 No Content)
     */
    @DeleteMapping("/{mbNo}")
    public ResponseEntity<Void> deleteMember(@PathVariable("mbNo") Long mbNo) {
        memberService.deleteMember(mbNo);

        return ResponseEntity.noContent().build();
    }

    /**
     * 회원의 비밀번호를 업데이트합니다.
     * <p>
     * 기존 비밀번호를 확인하고, 새로운 비밀번호가 유효한지 체크한 후, 비밀번호를 변경합니다.
     * </p>
     *
     * @param mbNo 회원의 고유 번호 (PathVariable)
     * @param request 비밀번호 업데이트에 필요한 정보 (새로운 비밀번호, 기존 비밀번호 등)
     * @return 내용 없는 응답 ResponseEntity (HTTP 204 No Content)
     */
    @PutMapping("/{mbNo}/password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable("mbNo") Long mbNo,
            @RequestBody @Valid MemberUpdatePasswordRequest request){
        memberService.updatePassword(mbNo, request);

        return ResponseEntity.noContent().build();
    }

    /**
     * 기본 회원 정보 목록을 페이징하여 조회합니다.
     * <p>
     * 이 메서드는 {@link MemberInfoResponse} DTO의 리스트와 함께 총 페이지 수, 전체 개수, 현재 페이지 번호를 포함하는
     * 커스텀 응답 객체 {@link MemberPageResponse}를 반환합니다.
     * </p>
     *
     * @param pageable 요청된 페이지 번호, 페이지 크기, 정렬 조건이 포함된 {@link Pageable} 객체
     * @return 페이징된 회원 기본 정보와 메타데이터를 포함한 {@link MemberPageResponse} 응답
     */
    @GetMapping
    ResponseEntity<MemberPageResponse> getMemberInfoList(@PageableDefault(size = 10) Pageable pageable) {
        Page<MemberInfoResponse> page = memberService.getMemberInfoList(pageable);
        return ResponseEntity.ok(
                new MemberPageResponse(
                        page.getContent(),
                        page.getTotalPages(),
                        page.getTotalElements(),
                        page.getNumber()
                )
        );
    }

    /**
     * 기본 회원 정보 목록을 조회합니다.
     * 이 메서드는 각 회원의 고유 번호를 리스트에 담아 반환합니다.
     *
     * @return 회원 고유 번호 리스트를 담은 ResponseEntity (HTTP 200 OK)
     */
    @GetMapping("/ids")
    ResponseEntity<List<MemberNoResponse>> getAllMemberIds() {
        List<MemberNoResponse> memberNoList = memberService.getAllMemberIds();
        return ResponseEntity.ok(memberNoList);
    }

    /**
     * 회원 비밀번호 인증을 수행하는 메서드.
     *
     * @param mbNo 회원 번호
     * @param request 비밀번호 확인을 위한 요청 객체. 해당 객체에는 비밀번호가 포함되어 있음.
     * @return {@link ResponseEntity} - 인증 결과를 포함하는 HTTP 응답.
     *         {@code true}일 경우 비밀번호가 맞고, {@code false}일 경우 비밀번호가 틀림.
     *
     * @throws IllegalArgumentException 만약 비밀번호가 유효하지 않은 경우
     */
    @PostMapping("/{mbNo}/password")
    public ResponseEntity<Boolean> verify(
            @PathVariable("mbNo") Long mbNo,
            @RequestBody @Valid MemberConfirmPasswordRequest request){
        boolean isValid =  memberService.verify(mbNo, request);

        return ResponseEntity.ok(isValid);
    }
}
