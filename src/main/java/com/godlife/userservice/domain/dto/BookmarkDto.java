package com.godlife.userservice.domain.dto;

import lombok.Data;

@Data
public class BookmarkDto {

	/** 피드 아이디 */
	private Long feedId;

	/** 북마크 상태 */
	private boolean bookmarkStatus;
}
