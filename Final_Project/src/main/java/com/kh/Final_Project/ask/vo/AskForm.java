package com.kh.Final_Project.ask.vo;

import java.time.LocalDateTime;

import com.kh.Final_Project.ask.entity.Ask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor

@Data
public class AskForm {

	private Long ocid;
	
	private Long memberId;
	
	private String custmerRealId;
	
	private String title;

	private String content;

	private String originalFile;
	
	private String renamedFile;
	
	private String imageFilePath;
	
	private String answerState;
	
	private LocalDateTime writeDate;
	
	public Ask toEntity() {
		return new Ask(ocid, memberId, custmerRealId, title, content, originalFile, renamedFile, imageFilePath, answerState, writeDate);
	}
}