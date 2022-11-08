package com.godlife.userservice.repository;

import com.godlife.userservice.domain.entity.Bookmark;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookmarkRepository extends CrudRepository<Bookmark, Long> {
    /** 회원 아이디와 피드 아이디로 북마크 조회 */
    Bookmark findByUser_UserIdAndFeedId(Long userId, Long feedId);
}
