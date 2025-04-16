package com.nhnacademy.memberservice.member.exception;

public class MemberEmailNotFoundException extends RuntimeException{

    public MemberEmailNotFoundException(String mbEmail){
        super(mbEmail + "을 찾을 수 없습니다.");
    }
}
