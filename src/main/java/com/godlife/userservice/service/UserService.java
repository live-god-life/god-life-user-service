package com.godlife.userservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.domain.entity.UserEntity;
import com.godlife.userservice.domain.request.RequestJoin;
import com.godlife.userservice.exception.UserException;
import com.godlife.userservice.repository.UserRepository;
import com.godlife.userservice.response.ApiResponse;
import com.godlife.userservice.response.ResponseCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    /** Api-Gateway Service URL */
    @Value("${url.apiGateway}")
    private String apiGatewayURL;

    /** WebClient 통신 Key (type) */
    private static final String TYPE_KEY = "type";

    /** WebClient 통신 Key (identifier) */
    private static final String IDENTIFIER_KEY = "identifier";

    /** 회원 repository */
    private final UserRepository userRepository;

    /** ObjectMapper */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 로그인을 위한 회원 여부 정보 조회
     * @param type          로그인 타입 (apple / kakao)
     * @param identifier    식별 값
     * @return 회원 정보
     */
    public UserDto getUserForLogin(String type, String identifier) {
        return Optional.ofNullable(userRepository.findByTypeAndIdentifier(type, identifier))
                       .map(user -> objectMapper.convertValue(user, UserDto.class))
                       .orElse(null);
    }

    /**
     * 닉네임 중복 체크
     * @param nickname  닉네임
     * @return 닉네임 중복 체크 결과
     */
    public ApiResponse checkNickname(String nickname) {
        // 닉네임이 누락된 경우 예외 처리
        if(!StringUtils.hasText(nickname)) {
            throw new UserException(ResponseCode.INVALID_PARAMETER);
        }

        // 해당 닉네임을 가진 회원 조회
        UserEntity user = userRepository.findByNickname(nickname);

        // 해당 닉네임을 가진 회원이 있는 경우 예외 처리
        if(user != null) {
            throw new UserException(ResponseCode.DUPLICATE_NICKNAME);
        }

        // 결과 반환
        return new ApiResponse(ResponseCode.NICKNAME_OK, null);
    }

    /**
     * 회원가입
     * @param requestData   회원가입 시 필요한 데이터
     * @return 회원가입 성공 및 로그인 처리 후 access token 반환
     */
    public String join(RequestJoin requestData) {

        // 파라미터 체크를 위한 HashMap 변환
        Map<String, String> requestMap = objectMapper.convertValue(requestData, HashMap.class);

        // 파라미터 null, 빈 값 체크
        if(requestMap.values().stream().anyMatch(value -> !StringUtils.hasText(value))) {
            throw new UserException(ResponseCode.INVALID_PARAMETER);
        }

        // 회원 정보 세팅
        UserEntity user = UserEntity.builder()
                                    .nickname(requestData.getNickname())
                                    .type(requestData.getType())
                                    .identifier(requestData.getIdentifier())
                                    .email(requestData.getEmail())
                                    .refreshToken(null)
                                    .build();

        // 회원가입
        userRepository.save(user);

        // auth-service 호출 (로그인 처리) -> access token 반환
        WebClient webClient = WebClient.create(apiGatewayURL);

        MultiValueMap<String, String> bodyData = new LinkedMultiValueMap<>();

        bodyData.add(TYPE_KEY, requestData.getType());
        bodyData.add(IDENTIFIER_KEY, requestData.getIdentifier());

        Map<String, String> responseData = objectMapper.convertValue(webClient.post()
                                                                              .uri("/login")
                                                                              .bodyValue(bodyData)
                                                                              .retrieve()
                                                                              .bodyToMono(ApiResponse.class)
                                                                              .block()
                                                                              .getData(), HashMap.class);

        return responseData.get("authorization");
    }

    /**
     * 사용자 정보 수정
     * @param userDto   사용자 정보
     */
    public void saveUserInfo(UserDto userDto) {
        // 파라미터 오류 시 예외 처리
        if(!StringUtils.hasText(String.valueOf(userDto.getUserId()))) {
            throw new UserException(ResponseCode.INVALID_PARAMETER);
        }

        UserEntity user = objectMapper.convertValue(userDto, UserEntity.class);
        userRepository.save(user);
    }
}
