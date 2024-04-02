package com.kh.Final_Project.event.vo;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventContent {
	
	private String title;
	private List<String> content;

}
