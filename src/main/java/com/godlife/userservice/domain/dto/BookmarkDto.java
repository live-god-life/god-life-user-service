package com.godlife.userservice.domain.dto;

import com.godlife.userservice.domain.entity.Users;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookmarkDto {

    /** 북마크 번호 */
    private Long bookmarkId;

    /** 회원 아이디 */
    private Users user;

    /** 피드 아이디 */
    private Long feedId;
}
