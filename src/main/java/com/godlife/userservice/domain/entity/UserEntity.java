package com.godlife.userservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@DynamicUpdate
@Table(name = "USERS")
public class UserEntity extends BaseEntity {

    /** 회원 번호 */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    /** 회원 로그인 식별 값 */
    @Column(unique = true, length = 1000)
    private String identifier;

    /** 회원 로그인 타입 */
    @Column
    private String type;

    /** 회원 닉네임 */
    @Column(unique = true)
    private String nickname;

    /** 회원 이메일 */
    @Column
    private String email;

    /** 회원 refresh token */
    @Column
    private String refreshToken;

    protected UserEntity() {}

    @Builder
    public UserEntity(Long userId, String identifier, String type, String nickname, String email, String refreshToken) {
        this.userId = userId;
        this.identifier = identifier;
        this.type = type;
        this.nickname = nickname;
        this.email = email;
        this.refreshToken = refreshToken;
    }
}
