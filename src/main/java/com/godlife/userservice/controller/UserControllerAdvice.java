package com.godlife.userservice.controller;

import com.godlife.userservice.exception.UserException;
import com.godlife.userservice.response.ApiResponse;
import com.godlife.userservice.response.ResponseCode;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Slf4j
public class UserControllerAdvice {
	/**
	 * UserException Handler
	 * @param e     UserException
	 * @return Exception 반환
	 */
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ApiResponse<?>> userExceptionHandler(UserException e) {
		// Exception 메시지 (ResponseCode 메시지)
		if (log.isInfoEnabled()) {
			log.info("Exception message: {}", e.getResponseCode().getMessage());
		}

		return ResponseEntity.status(e.getResponseCode().getHttpStatus()).body(new ApiResponse(e.getResponseCode(), null));
	}

	/**
	 * SQLIntegrityConstraintViolationException Handler
	 * @param e     SQLIntegrityConstraintViolationException
	 * @return Exception 반환
	 */
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ApiResponse<?>> uniqueExceptionHandler(SQLIntegrityConstraintViolationException e) {
		if (log.isErrorEnabled()) {
			log.error("Occurred SQLIntegrityConstraintViolationException");
		}

		return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse(ResponseCode.DUPLICATE_USER, null));
	}
}
