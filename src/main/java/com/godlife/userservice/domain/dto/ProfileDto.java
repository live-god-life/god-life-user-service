package com.godlife.userservice.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfileDto {

	/** 회원 닉네임 */
	private String nickname;

	/** 회원 이미지 */
	private String image;
}
