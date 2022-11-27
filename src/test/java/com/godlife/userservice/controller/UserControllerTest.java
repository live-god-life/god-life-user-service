package com.godlife.userservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.godlife.userservice.domain.dto.BookmarkDto;
import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.service.UserService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {UserController.class, UserService.class})
@AutoConfigureMockMvc
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;
	//
	//    @Test
	//    @DisplayName("회원 정보를 통한 회원 조회를 실패한다.")
	//    void failGetUserProfileByUserInfo() throws Exception {
	//
	//        // Given
	//        UserDto emptyUserInfo = new UserDto();
	//
	//        // When & Then
	//        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
	//                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
	//    }

	//    @Test
	//    @DisplayName("회원 아이디를 통한 회원 조회를 실패한다.")
	//    void failGetUserProfileByUserId() {
	//        // Given
	//        String userId = null;
	//
	//        // When
	//        ProfileDto user = userService.getUser(userId);
	//
	//        // Then
	//        assertThat(user).isNull();
	//    }

	//    @Test
	//    @DisplayName("북마크를 설정한다")
	//    void setBookmark() throws Exception {
	//
	//        UserDto user = UserDto.builder()
	//                .userId(1L)
	//                .build();
	//
	//        BookmarkDto bookmark = BookmarkDto.builder()
	//                                          .bookmarkId(1L)
	//                                                  .build();
	//
	//        mockMvc.perform(patch("/users/feeds/{feedId}/bookmark", 1)
	//                                .accept(MediaType.APPLICATION_JSON))
	//                                .andExpect(status().isOk())
	//                                .andDo(print());
	//    }
}
