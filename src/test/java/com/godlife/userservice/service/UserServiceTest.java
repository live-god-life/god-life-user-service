package com.godlife.userservice.service;

import com.godlife.userservice.domain.dto.ProfileDto;
import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.domain.entity.Users;
import com.godlife.userservice.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class UserServiceTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void insertUser() {
		Users user = Users.builder()
			.userId(1L)
			.nickname("hoon")
			.build();

		userRepository.save(user);
	}

	@Test
	@DisplayName("회원 정보를 통한 회원 조회를 성공한다.")
	void successGetUserProfileByUserInfo() {
		// Given
		UserDto userInfo = new UserDto();
		userInfo.setNickname("hoon");

		// When
		UserDto user = userService.getUser(userInfo);

		// Then
		assertThat(user.getUserId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("회원 아이디를 통한 회원 조회를 성공한다.")
	void successGetUserProfileByUserId() {
		// Given
		String userId = "1";

		// When
		ProfileDto user = userService.getUser(userId);

		// Then
		assertThat(user.getNickname()).isEqualTo("hoon");
	}
}
