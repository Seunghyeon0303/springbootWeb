package com.kh.Final_Project.event.entity;

import java.time.LocalDateTime;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Event{
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ocid;
	@Column
	private String title;
	@Column(length = 1000)
	private String content;
	@Column
	private String productUrl;
	@Column
	private String imageUrl;
	@Column
	private LocalDateTime writeDate;
	
    @PrePersist
    protected void onCreate() {
        if (writeDate == null) { // writeDate가 명시적으로 설정되지 않았다면 현재 시간으로 설정
            writeDate = LocalDateTime.now();
        }
    }
    
    // 제목, 내용 수정에 관한 메서드
	public void patch(Event event) {
		if(event.title != null) {
			//  수정할 title 입력되었는가?
		this.title = event.title;
		}
		if(event.content != null) {
			// 수정할 content 입력되었는가?
			this.content = event.content;
		}
		// 만약 입력된 데이터가 기존 데이터와 같으면 저장하지 않는다. 
		// 다르면 새로 저장하고, event에 저장하겠다.
	}

	// 데베 저장에 필요한 생성자
	public Event(String title, String content, String productUrl, String imageUrl) {
		this.title = title;
		this.content = content;
		this.productUrl = productUrl;
		this.imageUrl = imageUrl;
	}
	
}