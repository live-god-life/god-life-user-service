package com.godlife.userservice.repository;

import com.godlife.userservice.domain.entity.Bookmark;

import java.util.List;

public interface BookmarkCustomRepository {
	List<Bookmark> findByUserIdAndFeedId(Long userId, List<Long> feedIds);
}
