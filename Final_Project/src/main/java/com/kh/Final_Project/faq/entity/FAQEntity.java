package com.kh.Final_Project.faq.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class FAQEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ocid;
	@Column
	private String title;
	@Column(length = 5000)
	private String content;
	@Column
	private String category;
	@Column
	private LocalDateTime writeDate;
	
	 @PrePersist
	    protected void onCreate() {
	        if (writeDate == null) { // writeDate가 명시적으로 설정되지 않았다면 현재 시간으로 설정
	            writeDate = LocalDateTime.now();
	        }
	    }
	
	// 제목, 내용 수정에 관한 메서드
	public void patch(FAQEntity faq) {
		if(faq.title != null) {
			//  수정할 title 입력되었는가?
		this.title = faq.title;
		}
		if(faq.content != null) {
			// 수정할 content 입력되었는가?
			this.content = faq.content;
		}		
	}

	// 데베 저장에 필요한 생성자
	public FAQEntity(String title, String content, String category) {
		this.title = title;
		this.content = content;
		this.category = category;
	}	
}