package com.godlife.userservice.controller;

import com.godlife.userservice.domain.dto.UserDto;
import com.godlife.userservice.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = { UserController.class, UserService.class })
@AutoConfigureMockMvc
public class UserControllerTest {

//    @Autowired
//    private MockMvc mockMvc;
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
}
