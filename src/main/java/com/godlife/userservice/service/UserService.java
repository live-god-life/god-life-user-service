package com.godlife.userservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godlife.userservice.domain.dto.BookmarkDto;
import com.godlife.userservice.domain.dto.ProfileDto;
import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.domain.entity.Bookmark;
import com.godlife.userservice.domain.entity.Users;
import com.godlife.userservice.domain.request.RequestJoin;
import com.godlife.userservice.exception.UserException;
import com.godlife.userservice.repository.BookmarkRepository;
import com.godlife.userservice.repository.UserRepository;
import com.godlife.userservice.response.ApiResponse;
import com.godlife.userservice.response.ResponseCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	/** WebClient 통신 Key (userId) */
	private static final String USER_ID = "userId";

	/** 회원 repository */
	private final UserRepository userRepository;

	/** 북마크 repository */
	private final BookmarkRepository bookmarkRepository;

	/** ObjectMapper */
	private final ObjectMapper objectMapper = new ObjectMapper();

	/** Eureka LoadBalancer */
	private final LoadBalancerClient loadBalancerClient;

	/**
	 * 회원 조회 (로그인, 닉네임 조회 용)
	 * @param userDto   회원 조회 조건
	 * @return 회원 정보
	 */
	public UserDto getUser(UserDto userDto) {

		// 로그인용 회원 조회
		if (StringUtils.hasText(userDto.getType()) && StringUtils.hasText(userDto.getIdentifier())) {
			return Optional.ofNullable(userRepository.findByTypeAndIdentifier(userDto.getType(), userDto.getIdentifier()))
				.map(user -> objectMapper.convertValue(user, UserDto.class))
				.orElse(null);
		}

		// 닉네임으로 회원 조회
		if (StringUtils.hasText(userDto.getNickname())) {
			return Optional.ofNullable(userRepository.findByNickname(userDto.getNickname()))
				.map(user -> objectMapper.convertValue(user, UserDto.class))
				.orElse(null);
		}

		// 엑세스 토큰으로 회원 조회
		if(StringUtils.hasText(userDto.getAccessToken())) {
			return Optional.ofNullable(userRepository.findByAccessToken(userDto.getAccessToken()))
				.map(user -> objectMapper.convertValue(user, UserDto.class))
				.orElse(null);
		}

		return null;
	}

	/**
	 * 회원 조회 (여러 회원 조회 용)
	 * @param ids   회원 아이디 배열
	 * @return 회원 정보
	 */
	public List<UserDto> getUser(String[] ids) {
		// String ID 배열 -> Long ID List
		List<Long> idList = Arrays.stream(ids)
			.map(Long::parseLong)
			.collect(Collectors.toList());

		// 회원 리스트 생성
		List<UserDto> userList = userRepository.findByUserIdIn(idList)
			.stream()
			.map((user) -> objectMapper.convertValue(user, UserDto.class))
			.collect(Collectors.toList());

		return userList;
	}

	/**
	 * 회원 조회 (Front 용)
	 * @param userId    회원 아이디
	 * @return 회원 정보
	 */
	public ProfileDto getUser(String userId) {

		// 회원 아이디가 유효하지 않는 경우
		if (!(StringUtils.hasText(userId) && NumberUtils.isNumber(userId)))
			throw new UserException(ResponseCode.INVALID_PARAMETER);

		UserDto userInfo = Optional.ofNullable(userRepository.findByUserId(Long.parseLong(userId)))
			.map(user -> objectMapper.convertValue(user, UserDto.class))
			.orElse(null);

		// 회원 정보가 없는 경우
		if (userInfo == null)
			throw new UserException(ResponseCode.NOT_FOUND_USER);

		ProfileDto profile = ProfileDto.builder()
			.nickname(userInfo.getNickname())
			.image(userInfo.getImage())
			.build();

		return profile;
	}

	/**
	 * 찜한 글 조회
	 * @param userId    회원 아이디
	 * @return 찜한 글 리스트
	 */
	public Object getHeartFeeds(String userId) {

		// 회원의 북마크 정보 조회
		List<Bookmark> bookmark = bookmarkRepository.findByUser_UserId(Long.valueOf(userId));

		// 회원 아이디 매핑
		List<Long> bookmarkList = bookmark.stream()
			.map(Bookmark::getFeedId)
			.collect(Collectors.toList());

		// feed-service 호출 -> 피드 리스트 조회
		WebClient webClient = WebClient.create(loadBalancerClient.choose("FEED-SERVICE").getUri().toString());

		return webClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/feeds")
				.queryParam("ids", bookmarkList)
				.build())
			.header("x-user", userId)
			.retrieve()
			.bodyToMono(ApiResponse.class)
			.block()
			.getData();
	}

	/**
	 * 북마크 조회
	 * @param uid       회원 아이디
	 * @param fids      피드 아이디
	 * @return 북마크 정보
	 */
	public List<BookmarkDto> getBookmark(String uid, String[] fids) {
		Long userId = Long.parseLong(uid);
		List<Long> feedIds = Arrays.stream(fids).map(Long::parseLong).collect(Collectors.toList());

		// 회원의 북마크 조회
		List<Bookmark> bookmarkList = bookmarkRepository.findByUserIdAndFeedId(userId, feedIds);

		// 데이터 가공
		List<BookmarkDto> feedBookmarkList = new ArrayList<>();

		for (Long fid : feedIds) {
			BookmarkDto feedBookmark = new BookmarkDto();
			feedBookmark.setFeedId(fid);
			feedBookmark.setBookmarkStatus(bookmarkList.stream().anyMatch(bookmark -> bookmark.getFeedId().equals(fid)));

			feedBookmarkList.add(feedBookmark);
		}

		return feedBookmarkList;
	}

	/**
	 * 닉네임 중복 체크
	 * @param nickname  닉네임
	 * @return 닉네임 중복 체크 결과
	 */
	public ApiResponse<?> checkNickname(String nickname) {
		// 닉네임이 누락된 경우 예외 처리
		if (!StringUtils.hasText(nickname)) {
			throw new UserException(ResponseCode.INVALID_PARAMETER);
		}

		// 해당 닉네임을 가진 회원 조회
		Users user = userRepository.findByNickname(nickname);

		// 해당 닉네임을 가진 회원이 있는 경우 예외 처리
		if (user != null) {
			throw new UserException(ResponseCode.DUPLICATE_NICKNAME);
		}

		// 결과 반환
		return new ApiResponse<>(ResponseCode.NICKNAME_OK, null);
	}

	/**
	 * 회원가입
	 * @param requestData   회원가입 시 필요한 데이터
	 */
	@Transactional
	public void join(RequestJoin requestData) {

		// 타입명 리스트
		final List<String> TYPE = List.of("apple", "kakao");

		// 파라미터 체크를 위한 HashMap 변환
		Map<String, String> requestMap = objectMapper.convertValue(requestData, HashMap.class);

		// 파라미터 null, 빈 값 체크
		if (requestMap.values().stream().anyMatch(value -> !StringUtils.hasText(value)) || !TYPE.contains(requestData.getType())) {
			throw new UserException(ResponseCode.INVALID_PARAMETER);
		}

		// 회원 정보 세팅
		Users user = Users.builder()
			.nickname(requestData.getNickname())
			.type(requestData.getType())
			.identifier(requestData.getIdentifier())
			.email(requestData.getEmail())
			.accessToken(null)
			.refreshToken(null)
			.build();

		// 회원가입
		userRepository.save(user);
	}

	/**
	 * 회원가입
	 * @param requestData   회원가입 시 필요한 데이터
	 * @return 로그인 처리 후 access token 반환
	 */
	public String login(RequestJoin requestData) {

		// auth-service 호출 (로그인 처리)
		WebClient webClient = WebClient.create(loadBalancerClient.choose("AUTH-SERVICE").getUri().toString());

		Map<String, String> response = (Map<String, String>) webClient.post()
			.uri("/login")
			.bodyValue(requestData)
			.retrieve()
			.bodyToMono(ApiResponse.class)
			.block()
			.getData();

		return response.get("authorization");
	}

	/**
	 * 사용자 정보 수정
	 * @param userDto   사용자 정보
	 */
	@Transactional
	public void saveUserInfo(UserDto userDto) {
		// 파라미터 오류 시 예외 처리
		if (!StringUtils.hasText(String.valueOf(userDto.getUserId()))) {
			throw new UserException(ResponseCode.INVALID_PARAMETER);
		}

		Users user = userRepository.findByUserId(userDto.getUserId());

		// 데이터 수정
		if (StringUtils.hasText(userDto.getNickname()))
			user.changeNickname(userDto.getNickname());

		if (StringUtils.hasText(userDto.getImage()))
			user.changeImage(userDto.getImage());

		if (StringUtils.hasText(userDto.getAccessToken()) && StringUtils.hasText(userDto.getRefreshToken()))
			user.changeToken(userDto.getAccessToken(), userDto.getRefreshToken());
	}

	/**
	 * 북마크 설정
	 * @param uid       회원 아이디
	 * @param fid       피드 아이디
	 * @param status    상태 값
	 */
	public void saveBookmark(String uid, String fid, boolean status) {
		Long userId = Long.valueOf(uid);
		Long feedId = Long.valueOf(fid);

		Users user = userRepository.findByUserId(userId);
		Bookmark bookmark;

		if (status) {
			bookmark = bookmarkRepository.findByUser_UserIdAndFeedId(userId, feedId);

			if(bookmark != null) {
				throw new UserException(ResponseCode.INVALID_PARAMETER);
			}

			bookmark = Bookmark.builder()
				.user(user)
				.feedId(feedId)
				.build();

			bookmarkRepository.save(bookmark);
		} else {
			bookmark = bookmarkRepository.findByUser_UserIdAndFeedId(userId, feedId);

			if (bookmark == null) {
				throw new UserException(ResponseCode.INVALID_PARAMETER);
			}

			bookmarkRepository.delete(bookmark);
		}
	}

	/**
	 * 회원 탈퇴
	 * @param userId    회원 아이디
	 */
	public void deleteUser(Long userId) {
		Users user = userRepository.findByUserId(userId);

		userRepository.delete(user);
	}
}
