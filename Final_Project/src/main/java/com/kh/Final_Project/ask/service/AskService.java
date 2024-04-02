package com.kh.Final_Project.ask.service;

import org.springframework.data.domain.Page;


import com.kh.Final_Project.ask.entity.Ask;


public interface AskService {

	
	/* 페이지 처리 및 목록 (회원용-본인글만 조회)*/
	public Page<Ask> askListByMemberOcid(Long memberOcid, int page);

	/* 페이지 처리 및 목록(관리자용-전체조회) */
	public Page<Ask> askListAll(int page);

	/* 답변상태 변경하기 */
	public void updateAnswerState(Long ocid, String answerState);

	/* 해당 게시글&댓글 삭제*/
	public void deleteAll(Long ocid);
	
}