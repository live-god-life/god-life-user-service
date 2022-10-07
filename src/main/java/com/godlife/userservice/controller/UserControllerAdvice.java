package com.godlife.userservice.controller;

import com.godlife.userservice.exception.UserException;
import com.godlife.userservice.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class UserControllerAdvice {
    /**
     * UserException Handler
     * @param e     UserException
     * @return Exception 반환
     */
    @ExceptionHandler(UserException.class)
    public ResponseEntity<ApiResponse> exceptionHandler(UserException e) {
        // Exception 메시지 (ResponseCode 메시지)
        if(log.isInfoEnabled()) {
            log.info("Exception message: {}", e.getResponseCode().getMessage());
        }

        return ResponseEntity.status(e.getResponseCode().getHttpStatus()).body(new ApiResponse(e.getResponseCode(), null));
    }
}
