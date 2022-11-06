package com.godlife.userservice.controller;

import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.domain.request.RequestJoin;
import com.godlife.userservice.response.ApiResponse;
import com.godlife.userservice.response.ResponseCode;
import com.godlife.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
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
    public ResponseEntity<ApiResponse<?>> getUser(@RequestHeader(name = "x-user", required = false) String userId, UserDto userDto) {
        // 마이페이지 용
        if(StringUtils.hasText(userId)) {
            return ResponseEntity.ok(new ApiResponse<>(ResponseCode.PROFILE_OK, userService.getUser(userId)));
        }

        // 로그인 회원 조회용
        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.PROFILE_OK, userService.getUser(userDto)));
    }

    /**
     * 회원가입
     * @param requestData   회원가입 데이터
     * @return
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse<?>> createUser(RequestJoin requestData) {
        // bodyData 생성
        Map<String, String> bodyData = new HashMap<>(){{
            put(TOKEN_TYPE_KEY, "Bearer");
            put(AUTHORIZATION_KEY, userService.join(requestData));
        }};

        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.JOIN_OK, bodyData));
    }

    /**
     * 회원 수정
     * @param userDto   수정 할 사용자 정보
     * @return 회원 수정 결과
     */
    @PatchMapping("/users")
    public ResponseEntity<ApiResponse<?>> saveUserInfo(@RequestBody UserDto userDto) {
        userService.saveUserInfo(userDto);
        return ResponseEntity.ok(new ApiResponse<>(ResponseCode.REFRESH_TOKEN_SAVE_OK, null));
    }
}
