package com.kh.Final_Project.askAnswer.vo;

import java.time.LocalDateTime;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kh.Final_Project.ask.entity.Ask;
import com.kh.Final_Project.askAnswer.entity.AskAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AskAnswerForm {
	
	private Long askOcid;
	
	private String content;
	
	private LocalDateTime answerWriteDate;
	
	private Long boardNo;
	
	public AskAnswer toEntity() {
		return new AskAnswer(askOcid,content,answerWriteDate, boardNo);
	}
}