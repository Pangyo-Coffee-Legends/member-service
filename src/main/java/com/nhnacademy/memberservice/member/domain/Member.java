package com.nhnacademy.memberservice.member.domain;

import com.nhnacademy.memberservice.role.domain.Role;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Comment;

import java.time.LocalDateTime;

/**
 * 회원 정보를 저장하는 JPA 엔티티입니다.
 * <p>
 * 이 엔티티는 회원의 고유 번호, 이름, 이메일, 비밀번호, 전화번호, 역할(Role),
 * 생성일자 및 탈퇴일자와 같은 회원 정보를 포함합니다. 또한, 회원은 하나의 역할을 가질 수 있습니다.
 * </p>
 */
@Entity
@Table(name = "members")
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mb_no")
    @Comment("회원번호")
    private Long mbNo;


    @ManyToOne
    @JoinColumn(name = "role_no", nullable = false)
    private Role role;


    @Column(name = "mb_name", nullable = false, length = 50)
    @Comment("회원명")
    private String mbName;


    @Column(name = "mb_email", nullable = false, length = 100)
    @Comment("이메일")
    private String mbEmail;


    @ToString.Exclude
    @Column(name = "mb_password", nullable = false, length = 200)
    @Comment("비밀번호")
    private String mbPassword;


    @Column(nullable = false, length = 15)
    @Comment("전화번호")
    private String phoneNumber;


    @Comment("생성일자")
    private LocalDateTime createdAt;


    @Comment("탈퇴일자")
    private LocalDateTime withdrawnAt;


    private Member(String mbName, String mbEmail, String mbPassword, String  phoneNumber) {
        this.mbName = mbName;
        this.mbEmail = mbEmail;
        this.mbPassword = mbPassword;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 회원 생성에 사용되는 정적 팩토리 메서드입니다.
     * <p>
     * 이 메서드는 새로운 회원을 생성하기 위한 간편한 방법을 제공합니다.
     * </p>
     *
     * @param mbName      회원 이름
     * @param mbEmail     회원 이메일
     * @param mbPassword  회원 비밀번호
     * @param phoneNumber 회원 전화번호
     * @return 새로운 회원 객체
     */
    public static Member ofNewMember(String mbName, String mbEmail, String mbPassword, String phoneNumber) {
        return new Member(mbName, mbEmail, mbPassword, phoneNumber);
    }

    /**
     * 사용자의 비밀번호를 새 비밀번호로 변경합니다.
     *
     * @param newPassword 새로 설정할 비밀번호
     */
    public void updatePassword(String newPassword) {
        this.mbPassword = newPassword;
    }

    /**
     * 사용자 정보를 수정합니다.
     * 이름, 이메일, 비밀번호, 전화번호를 새 값으로 업데이트합니다.
     *
     * @param mbName       새 이름
     * @param mbEmail      새 이메일 주소
     * @param mbPassword   새 비밀번호
     * @param phoneNumber  새 전화번호
     */
    public void update(String mbName, String mbEmail, String mbPassword, String phoneNumber) {
        this.mbName = mbName;
        this.mbEmail = mbEmail;
        this.mbPassword = mbPassword;
        this.phoneNumber = phoneNumber;
    }

    /**
     * 회원 정보가 데이터베이스에 저장되기 전에 호출되는 메서드입니다.
     * 이 메서드는 생성일자를 자동으로 설정합니다.
     */
    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 회원 탈퇴를 처리하는 메서드입니다.
     * 탈퇴 시 탈퇴일자가 현재 시각으로 설정됩니다.
     */
    public void withdraw() {
        this.withdrawnAt = LocalDateTime.now();
    }

    /**
     * 회원에게 역할을 부여하는 메서드입니다.
     * <p>
     * 이 메서드는 {@link Role} 엔티티를 회원에게 할당합니다.
     * </p>
     *
     * @param role 부여할 역할
     */
    public void assignRole(Role role) {
        this.role = role;
    }

    public Long getMbNo() {
        return mbNo;
    }

    public Role getRole() {
        return role;
    }

    public String getMbName() {
        return mbName;
    }

    public String getMbEmail() {
        return mbEmail;
    }

    public String getMbPassword() {
        return mbPassword;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getWithdrawnAt() {
        return withdrawnAt;
    }

    /**
     * 해당 회원이 탈퇴 상태인지 확인합니다.
     *
     * @return 탈퇴일자(withdrawnAt)가 null이 아니면 true, 그렇지 않으면 false
     */
    public boolean isWithdrawn() {
        return this.withdrawnAt != null;
    }



}

