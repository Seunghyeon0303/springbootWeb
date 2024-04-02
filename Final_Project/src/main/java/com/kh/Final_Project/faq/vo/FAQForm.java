package com.kh.Final_Project.faq.vo;

import com.kh.Final_Project.faq.entity.FAQEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FAQForm {
	
	private Long ocid;
	private String title;
	private String content;
	private String category;
	
	public FAQEntity toEntity() {
		return new FAQEntity(ocid,title,content,category, null);
	}
}