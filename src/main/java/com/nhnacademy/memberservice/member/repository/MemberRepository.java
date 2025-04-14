package com.nhnacademy.memberservice.member.repository;

import com.nhnacademy.memberservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 회원 관련 데이터베이스 작업을 수행하는 JPA 리포지토리 인터페이스입니다.
 * <p>
 * 이 인터페이스는 {@link Member} 엔티티에 대한 CRUD(생성, 조회, 수정, 삭제) 작업을 수행합니다.
 * JpaRepository를 상속하여 기본적인 데이터베이스 작업을 자동으로 제공하며,
 * 추가적인 데이터베이스 쿼리를 정의할 수 있습니다.
 * </p>
 */
public interface MemberRepository extends JpaRepository<Member, Long> {
}
