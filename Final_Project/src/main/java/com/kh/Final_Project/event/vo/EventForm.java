package com.kh.Final_Project.event.vo;


import com.kh.Final_Project.event.entity.Event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class EventForm {

	private Long ocid;
	private String title;
	private String content;
	private String productUrl;
	private String imageUrl;

	public Event toEntity() {
		return new Event(ocid, title, content, productUrl, imageUrl, null);
	}

}