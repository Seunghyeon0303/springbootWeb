package com.kh.Final_Project.askAnswer.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;

import com.kh.Final_Project.ask.entity.Ask;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class AskAnswer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long askOcid;
	
	@NonNull
	@Column(length=1000)
	private String content;
	
	@Column
	private LocalDateTime answerWriteDate;
	
	@Column
    private Long boardNo;
	
    @PrePersist
    protected void onCreate() {
        if (answerWriteDate == null) { // answerWriteDate가 명시적으로 설정되지 않았다면 현재 시간으로 설정
        	answerWriteDate = LocalDateTime.now();
        }
    }

}