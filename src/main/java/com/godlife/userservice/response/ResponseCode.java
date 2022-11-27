package com.godlife.userservice.response;

import lombok.Getter;

import org.springframework.http.HttpStatus;

@Getter
public enum ResponseCode {
	NICKNAME_OK("success", null, "사용할 수 있는 닉네임입니다.", HttpStatus.OK),                           // 닉네임 중복 x
	JOIN_OK("success", null, "회원가입을 성공했습니다.", HttpStatus.OK),                                  // 회원가입 성공
	UPDATE_OK("success", null, "프로필 정보를 수정했습니다.", HttpStatus.OK),                              // 프로필 수정 성공
	PROFILE_OK("success", null, "프로필 정보를 조회했습니다.", HttpStatus.OK),                             // 프로필 조회 성공
	BOOKMARK_REGIST_OK("success", null, "북마크 등록을 성공했습니다.", HttpStatus.OK),                     // 북마크 등록 성공
	BOOKMARK_DELETE_OK("success", null, "북마크 제거를 성공했습니다.", HttpStatus.OK),                     // 북마크 제거 성공
	BOOKMARK_OK("success", null, "북마크 정보를 조회했습니다.", HttpStatus.OK),                            // 북마크 조회 성공
	BOOKMARK_FEEDS_OK("success", null, "피드를 조회했습니다.", HttpStatus.OK),                           // 찜한 글 조회 성공
	DELETE_USER_OK("success", null, "회원탈퇴 되었습니다.", HttpStatus.OK),                              // 회원 탈퇴 성공

	// 실패 코드
	INVALID_PARAMETER("error", 400, "올바른 정보가 아닙니다.", HttpStatus.BAD_REQUEST),                   // 파라미터 오류
	DUPLICATE_NICKNAME("error", 402, "중복된 닉네임입니다.", HttpStatus.CONFLICT),                       // 닉네임 중복
	DUPLICATE_USER("error", 402, "이미 가입된 회원입니다.", HttpStatus.CONFLICT),                        // 회원 중복
	NOT_FOUND_USER("error", 401, "프로필 정보 조회를 실패했습니다.", HttpStatus.NOT_FOUND);                	// 프로필 조회 실패


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
