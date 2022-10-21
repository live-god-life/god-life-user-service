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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService{

    /** Api-Gateway Service URL */
    @Value("${url.apiGateway}")
    private String apiGatewayURL;

    /** WebClient 통신 Key (name) */
    private static final String NAME = "name";

    /** 회원 repository */
    private final UserRepository userRepository;

    /** ObjectMapper */
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 회원 조회
     * @param userDto   회원 조회 조건
     * @return 회원 정보
     */
    public UserDto getUser(UserDto userDto) {
        if(StringUtils.hasText(userDto.getType()) && StringUtils.hasText(userDto.getIdentifier())) {
            return Optional.ofNullable(userRepository.findByTypeAndIdentifier(userDto.getType(), userDto.getIdentifier()))
                           .map(user -> objectMapper.convertValue(user, UserDto.class))
                           .orElse(null);
        }

        if(StringUtils.hasText(userDto.getNickname())) {
            return Optional.ofNullable(userRepository.findByNickname(userDto.getNickname()))
                           .map(user -> objectMapper.convertValue(user, UserDto.class))
                           .orElse(null);
        }

        return null;
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
    @Transactional
    public String join(RequestJoin requestData) {

        // 타입명 리스트
        final List<String> TYPE = List.of("apple", "kakao");

        // 파라미터 체크를 위한 HashMap 변환
        Map<String, String> requestMap = objectMapper.convertValue(requestData, HashMap.class);

        // 파라미터 null, 빈 값 체크
        if(requestMap.values().stream().anyMatch(value -> !StringUtils.hasText(value)) || !TYPE.contains(requestData.getType())) {
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

        return String.valueOf(webClient.get()
                             .uri(uriBuilder -> uriBuilder
                                     .path("/tokens")
                                     .queryParam(NAME, user.getNickname())
                                     .build())
                             .retrieve()
                             .bodyToMono(ApiResponse.class)
                             .block()
                             .getData());
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
