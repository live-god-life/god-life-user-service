package com.godlife.userservice.controller;

import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.domain.request.RequestJoin;
import com.godlife.userservice.response.ApiResponse;
import com.godlife.userservice.response.ResponseCode;
import com.godlife.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
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
     * 로그인 용 사용자 조회 (토큰 검사 x)
     * @param type          로그인 타입
     * @param identifier    로그인 식별 값
     * @return 사용자 정보
     */
    @GetMapping("/user")
    public ResponseEntity<UserDto> getUser(String type, String identifier) {
        return ResponseEntity.ok(userService.getUserForLogin(type, identifier));
    }

    /**
     * 닉네임 중복 체크
     * @param nickname  닉네임
     * @return 닉네임 중복체크 결과
     */
    @GetMapping(value = {"/nickname", "/nickname/{nickname}"})
    public ResponseEntity<ApiResponse> chkNickname(@PathVariable(required = false) String nickname) {
        return ResponseEntity.ok(userService.chkNickName(nickname));
    }

    /**
     * 회원가입
     * @param requestData   회원가입 데이터
     * @return
     */
    @PostMapping("/users")
    public ResponseEntity<ApiResponse> createUser(RequestJoin requestData) {
        // bodyData 생성
        Map<String, String> bodyData = new HashMap<>(){{
            put(TOKEN_TYPE_KEY, "Bearer");
            put(AUTHORIZATION_KEY, userService.join(requestData));
        }};

        return ResponseEntity.ok(new ApiResponse(ResponseCode.JOIN_OK, bodyData));
    }

    /**
     * 회원 수정
     * @param userDto   수정 할 사용자 정보
     * @return 회원 수정 결과
     */
    @PutMapping("/users")
    public ResponseEntity<ApiResponse> saveUserInfo(@RequestBody UserDto userDto) {
        userService.saveUserInfo(userDto);
        return ResponseEntity.ok(new ApiResponse(ResponseCode.REFRESH_TOKEN_SAVE_OK, null));
    }
}
