package com.nhnacademy.memberservice.member.exception;

public class MemberAlreadyExistsException extends RuntimeException {
    public MemberAlreadyExistsException(String email) {
        super("%s: 회원이 이미 존재합니다.".formatted(email));
    }
}
