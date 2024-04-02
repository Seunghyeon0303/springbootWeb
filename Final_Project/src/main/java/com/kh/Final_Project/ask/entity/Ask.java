package com.kh.Final_Project.ask.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class Ask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long ocid; // 게시글의 고유번호
	
	@Column
	@NonNull
	private Long memberId; // 회원가입시 받는 회원의 고유번호
	
	@Column
	@NonNull
	private String custmerRealId; // 회원의 Id
	
	@Column
	@NonNull
	private String title;
	
	@Column(length=1000)
	@NonNull
	private String content;
	
	@Column
	private String originalFile; // 실제 업로드 파일명
	
	@Column
	private String renamedFile; // 서버에 저장된 파일명
	
	@Column
	private String imageFilePath; // 경로
	
	@Column
	private String answerState;
	
	@Column
	private LocalDateTime writeDate;
	
    @PrePersist
    protected void onCreate() {
        if (writeDate == null) { // writeDate가 명시적으로 설정되지 않았다면 현재 시간으로 설정
            writeDate = LocalDateTime.now();
        }
    }

	
}