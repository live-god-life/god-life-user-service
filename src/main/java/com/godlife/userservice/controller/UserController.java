package com.godlife.userservice.controller;

import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.domain.request.RequestJoin;
import com.godlife.userservice.domain.request.RequestSetBookmark;
import com.godlife.userservice.response.ApiResponse;
import com.godlife.userservice.response.ResponseCode;
import com.godlife.userservice.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class UserController {

	/** 로그인 결과 body data key (token_type) */
	private static final String TOKEN_TYPE_KEY = "token_type";

	/** 로그인 결과 body data key (authorization) */
	private static final String AUTHORIZATION_KEY = "authorization";

	/** 회원 관련 서비스 */
	private final UserService userService;

	/**
	 * 닉네임 중복 체크
	 * @param nickname  닉네임
	 * @return 닉네임 중복체크 결과
	 */
	@GetMapping(value = {"/nickname", "/nickname/{nickname}"})
	public ResponseEntity<ApiResponse<?>> checkNickname(@PathVariable(required = false) String nickname) {
		return ResponseEntity.ok(userService.checkNickname(nickname));
	}

	/**
	 * 회원 조회
	 * @param userDto     회원 조회 조건
	 * @return 회원 정보
	 */
	@GetMapping("/users")
	public ResponseEntity<ApiResponse<?>> getUser(@RequestHeader(name = "x-user", required = false) String userId, UserDto userDto, String[] ids) {

		// 여러 회원 조회 용
		if (ids != null) {
			return ResponseEntity.ok(new ApiResponse<>(ResponseCode.PROFILE_OK, userService.getUser(ids)));
		}

		// 마이페이지 용
		if (StringUtils.hasText(userId)) {
			return ResponseEntity.ok(new ApiResponse<>(ResponseCode.PROFILE_OK, userService.getUser(userId)));
		}

		// 로그인 회원 조회용
		return ResponseEntity.ok(new ApiResponse<>(ResponseCode.PROFILE_OK, userService.getUser(userDto)));
	}

	/**
	 * 찜한 글 조회
	 * @param userId    회원 아이디
	 * @return 찜한 글 리스트
	 */
	@GetMapping("/users/hearts")
	public ResponseEntity<ApiResponse<?>> getFeeds(@RequestHeader("x-user") String userId) {
		return ResponseEntity.ok(new ApiResponse<>(ResponseCode.BOOKMARK_FEEDS_OK, userService.getHeartFeeds(userId)));
	}

	/**
	 * 북마크 조회
	 * @param userId        회원 아이디
	 * @param feedIds       피드 아이디
	 * @return 북마크 정보
	 */
	@GetMapping("/users/{userId}/bookmarks")
	public ResponseEntity<ApiResponse<?>> getBookmark(@PathVariable String userId, String[] feedIds) {
		return ResponseEntity.ok(new ApiResponse<>(ResponseCode.BOOKMARK_OK, userService.getBookmark(userId, feedIds)));
	}

	/**
	 * 회원가입
	 * @param requestData   회원가입 데이터
	 * @return 회원가입 결과
	 */
	@PostMapping("/users")
	public ResponseEntity<ApiResponse<?>> createUser(@RequestBody RequestJoin requestData) {

		// 회원 가입
		userService.join(requestData);

		// bodyData 생성
		Map<String, String> bodyData = new HashMap<>() {{
			put(TOKEN_TYPE_KEY, "Bearer");
			put(AUTHORIZATION_KEY, userService.login(requestData));
		}};

		return ResponseEntity.ok(new ApiResponse<>(ResponseCode.JOIN_OK, bodyData));
	}

	/**
	 * 회원 수정
	 * @param userDto   수정 할 사용자 정보
	 * @return 회원 수정 결과
	 */
	@PatchMapping("/users")
	public ResponseEntity<ApiResponse<?>> saveUserInfo(@RequestHeader(value = "x-user", required = false) String userId, @RequestBody UserDto userDto) {
		if (StringUtils.hasText(userId))
			userDto.setUserId(Long.valueOf(userId));

		userService.saveUserInfo(userDto);
		return ResponseEntity.ok(new ApiResponse<>(ResponseCode.UPDATE_OK, null));
	}

	/**
	 * 북마크 설정
	 * @param userId            회원 아이디
	 * @param feedId            피드 아이디
	 * @param bookmark          설정할 북마크 정보
	 * @return 북마크 설정 결과
	 */
	@PatchMapping("/users/feeds/{feedId}/bookmark")
	public ResponseEntity<ApiResponse<?>> saveBookmark(@RequestHeader("x-user") String userId, @PathVariable String feedId, @RequestBody RequestSetBookmark bookmark) {
		boolean status = bookmark.isBookmarkStatus();
		userService.saveBookmark(userId, feedId, status);

		ResponseCode code = status ? ResponseCode.BOOKMARK_REGIST_OK : ResponseCode.BOOKMARK_DELETE_OK;
		return ResponseEntity.ok(new ApiResponse<>(code, null));
	}

	/**
	 * 회원 탈퇴
	 * @param userId        회원 아이디
	 * @return 회원 탈퇴 결과
	 */
	@DeleteMapping("/users")
	public ResponseEntity<ApiResponse<?>> deleteUser(@RequestHeader("x-user") Long userId) {
		userService.deleteUser(userId);
		return ResponseEntity.ok(new ApiResponse<>(ResponseCode.DELETE_USER_OK, null));
	}
}
