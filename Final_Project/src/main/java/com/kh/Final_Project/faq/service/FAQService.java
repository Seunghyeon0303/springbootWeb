package com.kh.Final_Project.faq.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kh.Final_Project.faq.entity.FAQEntity;
import com.kh.Final_Project.faq.vo.FAQForm;

public interface FAQService {

	// FAQ 전체 조회
	Page<FAQEntity> findAll(Pageable pageable);
	
	// FAQ 1건 조회
	FAQEntity findFaqByOcid(Long ocid);
	
	// FAQ 게시글 수정
	FAQEntity edit(Long ocid, FAQForm faqVo);
	FAQEntity saveFaq(FAQEntity faq);
	
	// FAQ 게시글 삭제
	FAQEntity deleteFaq(Long ocid);
	
	// FAQ 게시글 생성
	FAQEntity createFaq(FAQEntity faq);

	// 데이터 데베 저장
	void resetFaqData();
}