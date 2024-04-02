package com.kh.Final_Project.notice.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kh.Final_Project.notice.entity.NoticeEntity;
import com.kh.Final_Project.notice.vo.NoticeForm;

public interface NoticeService {
	// 공지사항 전체 조회 
	Page<NoticeEntity> findAll(Pageable pageable);

	// 공지사항 1건 조회
	NoticeEntity findNoticeByOcid(Long ocid);

	// 공지사항 게시글 생성
	NoticeEntity createNotice(NoticeEntity notice);

	// 공지사항 게시글 수정
	NoticeEntity edit(Long ocid, NoticeForm noticeVo);
	NoticeEntity saveNotice(NoticeEntity notice);

	// 공지사항 게시글 삭제
	NoticeEntity deleteNotice(Long ocid);

	// 데이터 데베 저장
	void resetNoticeData();
}
