package com.godlife.userservice.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;
@Getter
public enum ResponseCode {
    NICKNAME_OK("success", null, "사용할 수 있는 닉네임입니다.", HttpStatus.OK),                           // 닉네임 중복 x
    JOIN_OK("success", null, "회원가입을 성공했습니다.", HttpStatus.OK),                                  // 회원가입 성공
    REFRESH_TOKEN_SAVE_OK("success", null, "Refresh Token을 저장했습니다.", HttpStatus.OK),             // Refresh Token 저장 성공

    // 실패 코드
    INVALID_PARAMETER("error", 400, "올바른 정보가 아닙니다.", HttpStatus.BAD_REQUEST),                   // 파라미터 오류
    DUPLICATE_NICKNAME("error", 402, "중복된 닉네임입니다.", HttpStatus.CONFLICT);                       // 닉네임 중복

    /** 상태 (success / error) */
    private String status;

    /** 오류 코드 */
    private Integer code;

    /** 오류 메시지 */
    private String message;

    /** Http 상태 코드 */
    private HttpStatus httpStatus;

    ResponseCode(String status, Integer code, String message, HttpStatus httpStatus) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }
}
