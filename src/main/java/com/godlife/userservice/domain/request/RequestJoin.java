package com.godlife.userservice.domain.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestJoin {
	/** 회원 닉네임 */
	private String nickname;

	/** 회원 로그인 타입 */
	private String type;

	/** 회원 로그인 식별 값 */
	private String identifier;

	/** 회원 이메일 */
	private String email;
}
