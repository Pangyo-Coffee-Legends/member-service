package com.nhnacademy.memberservice.member.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    @DisplayName("비밀번호는 암호화되어 원본과 다르게 저장되어야 한다")
    void encodePassword_shouldBeHashed() {
        String rawPassword = "mySecret123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(encodedPassword).startsWith("$2a$"); // BCrypt 해시 포맷 확인
    }

    @Test
    @DisplayName("암호화된 비밀번호는 원본 비밀번호와 비교 시 일치해야 한다")
    void encodedPassword_shouldMatchRawPassword() {
        String rawPassword = "mySecret123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        boolean matches = passwordEncoder.matches(rawPassword, encodedPassword);
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("잘못된 비밀번호는 일치하지 않아야 한다")
    void wrongPassword_shouldNotMatch() {
        String rawPassword = "mySecret123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        boolean matches = passwordEncoder.matches("wrongPassword", encodedPassword);
        assertThat(matches).isFalse();
    }
}
