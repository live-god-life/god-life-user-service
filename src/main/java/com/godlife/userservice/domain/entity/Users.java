package com.godlife.userservice.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
public class Users extends BaseEntity {

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
	@Comment("회원 access token")
	private String accessToken;

	@Column
	@Comment("회원 refresh token")
	private String refreshToken;

	@Column
	@Comment("회원 프로필 이미지")
	private String image;

	protected Users() {
	}

	@Builder
	public Users(Long userId, String identifier, String type, String nickname, String email, String accessToken, String refreshToken, String image) {
		this.userId = userId;
		this.identifier = identifier;
		this.type = type;
		this.nickname = nickname;
		this.email = email;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.image = image;
	}

	/**
	 * 닉네임 변경 메소드
	 * @param nickname
	 */
	public void changeNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 이미지 변경 메소드
	 * @param image
	 */
	public void changeImage(String image) {
		this.image = image;
	}

	/**
	 * token 변경 메소드
	 * @param accessToken
	 * @param refreshToken
	 */
	public void changeToken(String accessToken, String refreshToken) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
