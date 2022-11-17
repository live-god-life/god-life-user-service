package com.godlife.userservice.domain.request;

import lombok.Data;

@Data
public class RequestSetBookmark {

	/** 설정할 북마크 상태 */
	private boolean bookmarkStatus;
}
