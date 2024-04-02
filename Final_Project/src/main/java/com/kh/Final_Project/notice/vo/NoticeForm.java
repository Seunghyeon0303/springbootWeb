package com.kh.Final_Project.notice.vo;

import com.kh.Final_Project.notice.entity.NoticeEntity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NoticeForm {
	private Long ocid;
	private String title;
	private String content;	

	public NoticeEntity toEntity() {
		return new NoticeEntity(ocid, title, content, null);
	}
	
}