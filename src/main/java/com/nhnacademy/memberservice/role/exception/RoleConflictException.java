package com.nhnacademy.memberservice.role.exception;

public class RoleConflictException extends RuntimeException {
    public RoleConflictException(String roleName) {
        super("권한 이름이 중복됩니다. " + roleName);
    }
}
