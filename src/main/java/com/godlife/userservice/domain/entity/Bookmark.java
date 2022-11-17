package com.godlife.userservice.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Comment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BOOKMARK")
public class Bookmark extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Comment("북마크 번호")
	private Long bookmarkId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "USER_ID")
	@Comment("회원 아이디")
	private Users user;

	@Column
	@Comment("피드 아이디")
	private Long feedId;

	@Builder
	protected Bookmark(Long bookmarkId, Users user, Long feedId) {
		this.bookmarkId = bookmarkId;
		this.user = user;
		this.feedId = feedId;
	}
}
