package com.godlife.userservice.exception;

import com.godlife.userservice.response.ResponseCode;

import lombok.Getter;

@Getter
public class UserException extends RuntimeException {

	/**
	 * Client 응답 시 사용할 코드
	 */
	private ResponseCode responseCode;

	public UserException(ResponseCode responseCode) {
		this.responseCode = responseCode;
	}
}
