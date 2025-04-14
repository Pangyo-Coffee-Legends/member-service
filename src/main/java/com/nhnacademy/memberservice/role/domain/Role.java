package com.nhnacademy.memberservice.role.domain;

import com.nhnacademy.memberservice.member.domain.Member;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.util.ArrayList;
import java.util.List;

/**
 * 권한 정보를 저장하는 JPA 엔티티입니다.
 * <p>
 * 이 엔티티는 사용자의 역할을 정의하는 역할을 합니다. 역할 번호, 역할 이름,
 * 역할 설명 등의 정보를 포함합니다.
 * </p>
 */
@Entity
@Table(name = "roles")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_no", nullable = false)
    @Comment("권한번호")
    private Long roleNo;

    @Column(name = "role_name", nullable = false, length = 50)
    @Comment("권한명")
    private String roleName;

    @Column(name = "role_description", nullable = false, length = 200)
    @Comment("권한설명")
    private String roleDescription;

    @OneToMany(mappedBy = "role")
    private List<Member> members = new ArrayList<>();


    private Role(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public static Role ofNewRole(String roleName, String roleDescription){
        return new Role(roleName, roleDescription);
    }

    /**
     * 권한 정보를 업데이트하는 메서드입니다.
     * 이 메서드는 역할 이름과 역할 설명을 수정할 때 사용됩니다.
     *
     * @param roleName 수정할 권한 이름
     * @param roleDescription 수정할 권한 설명
     */
    public void update(String roleName, String roleDescription) {
        this.roleName = roleName;
        this.roleDescription = roleDescription;
    }

    public Long getRoleNo() {
        return roleNo;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public List<Member> getMembers() {
        return members;
    }
}

