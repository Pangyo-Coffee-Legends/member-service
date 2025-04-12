package com.nhnacademy.memberservice.role.repository;

import com.nhnacademy.memberservice.role.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * 역할(Role) 엔티티에 대한 데이터 접근을 담당하는 리포지토리 인터페이스입니다.
 * <p>
 * Spring Data JPA의 {@link JpaRepository}를 상속받아,
 * 기본적인 CRUD(Create, Read, Update, Delete) 기능을 제공합니다.
 * </p>
 *
 * <p>
 * 필요 시 사용자 정의 쿼리 메서드를 추가하여 확장할 수 있습니다.
 * </p>
 */
public interface RoleRepository extends JpaRepository<Role, Long> {
    // 기본 CRUD 메서드는 JpaRepository에서 제공되므로 추가적인 메서드는 필요하지 않습니다.

    /**
     * 주어진 역할 이름에 해당하는 Role을 조회합니다.
     *
     * @param roleName 역할 이름 (예: "USER", "ADMIN")
     * @return 역할 정보가 담긴 Optional Role 객체
     */
    Optional<Role> findByRoleName(String roleName);
}
