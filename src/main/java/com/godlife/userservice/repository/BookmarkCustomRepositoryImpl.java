package com.godlife.userservice.repository;

import com.godlife.userservice.domain.entity.Bookmark;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.godlife.userservice.domain.entity.QBookmark.bookmark;

@Repository
@RequiredArgsConstructor
public class BookmarkCustomRepositoryImpl implements BookmarkCustomRepository {

	/** QueryDSL Factory */
	private final JPAQueryFactory queryFactory;

	/**
	 * 회원 아이디와 피드 아이디로 북마크 조회
	 * @param userId        회원 아이디
	 * @param feedIds       피드 아이디
	 * @return 북마크 정보
	 */
	@Override
	public List<Bookmark> findByUserIdAndFeedId(Long userId, List<Long> feedIds) {
		List<Bookmark> bookmarkList = queryFactory.selectFrom(bookmark)
			.where(bookmark.user.userId.eq(userId))
			.where(bookmark.feedId.in(feedIds))
			.fetch();

		return bookmarkList;
	}
}
