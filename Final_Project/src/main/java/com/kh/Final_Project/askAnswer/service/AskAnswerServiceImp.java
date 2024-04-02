package com.kh.Final_Project.askAnswer.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.kh.Final_Project.askAnswer.entity.AskAnswer;
import com.kh.Final_Project.askAnswer.repository.AskAnswerRepository;
import com.kh.Final_Project.askAnswer.vo.AskAnswerForm;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AskAnswerServiceImp implements AskAnswerService{
	
	@Autowired
	private AskAnswerRepository askAnswerRepository;
	
	/* 답변작성 */
	public void answerWrite(Long askBoardNO, String content) {
		
		// 답변작성
		AskAnswerForm answerForm = new AskAnswerForm();
		answerForm.setBoardNo(askBoardNO);
		answerForm.setContent(content);
		AskAnswer askAnswer = answerForm.toEntity();
		// DB에 추가
		askAnswerRepository.save(askAnswer);
	
	}
	
	
	public List<AskAnswer> askAnswerList(Long ocid) {

		return askAnswerRepository.findByBoardNo(ocid);
	}

}