package com.nhnacademy.memberservice.member.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 비밀번호 암호화를 위한 설정 클래스입니다.
 * <p>
 * {@link BCryptPasswordEncoder}를 스프링 빈으로 등록하여
 * 회원 가입, 로그인, 비밀번호 변경 등의 인증 로직에서
 * 일관된 방식의 해시 암호화를 적용할 수 있도록 합니다.
 * </p>
 * <p>
 * 이 설정을 통해 {@code PasswordEncoder}를 주입받아 사용하는 모든 클래스에서
 * 동일한 해시 로직을 공유할 수 있습니다.
 * </p>
 *
 * @author
 */
@Configuration
public class PasswordConfig {

    /**
     * {@link PasswordEncoder} 구현체로서 {@link org.springframework.security.crypto.password.DelegatingPasswordEncoder}를 반환합니다.
     * <p>
     * BCrypt는 보안성이 검증된 해시 방식
     * 동일한 비밀번호라도 매번 다른 결과를 생성합니다.
     * </p>
     *
     * @return {@code DelegatingPasswordEncoder}
     */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
