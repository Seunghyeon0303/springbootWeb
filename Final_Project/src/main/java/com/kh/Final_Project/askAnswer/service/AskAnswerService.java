package com.kh.Final_Project.askAnswer.service;

import java.util.List;

import com.kh.Final_Project.askAnswer.entity.AskAnswer;

public interface AskAnswerService {

	/* 답변작성 */
	public void answerWrite(Long askBoardNO, String content);
	
	public List<AskAnswer> askAnswerList(Long ocid);
}