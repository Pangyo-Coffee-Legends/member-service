package com.nhnacademy.memberservice.member.exception;

public class PasswordNotMatchException extends RuntimeException {
    public PasswordNotMatchException() {
        super("기존 비밀번호가 일치하지 않습니다.");
    }
}
