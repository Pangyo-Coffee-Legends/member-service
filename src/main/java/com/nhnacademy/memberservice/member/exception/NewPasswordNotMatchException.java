package com.nhnacademy.memberservice.member.exception;

public class NewPasswordNotMatchException extends RuntimeException {
    public NewPasswordNotMatchException() {
        super("새로운 비밀번호와 비밀번호 확인이 일치하지 않습니다.");
    }
}
