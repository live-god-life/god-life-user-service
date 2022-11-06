package com.godlife.userservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.Comment;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("회원 번호")
    private Long userId;

    @Column(unique = true, length = 1000)
    @Comment("회원 로그인 식별 값")
    private String identifier;

    @Column
    @Comment("회원 로그인 타입")
    private String type;

    @Column(unique = true)
    @Comment("회원 닉네임")
    private String nickname;

    @Column
    @Comment("회원 이메일")
    private String email;

    @Column
    @Comment("회원 refresh token")
    private String refreshToken;

    @Column
    @Comment("회원 프로필 이미지")
    private String image;

    protected UserEntity() {}

    @Builder
    public UserEntity(Long userId, String identifier, String type, String nickname, String email, String refreshToken, String image) {
        this.userId = userId;
        this.identifier = identifier;
        this.type = type;
        this.nickname = nickname;
        this.email = email;
        this.refreshToken = refreshToken;
        this.image = image;
    }
}
