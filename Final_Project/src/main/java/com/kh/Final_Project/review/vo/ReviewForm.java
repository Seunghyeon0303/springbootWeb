package com.kh.Final_Project.review.vo;

import com.kh.Final_Project.review.entity.Review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewForm {
	
	private Long reviewId;
	private Long productOcid;
	private double rating;
	private String reviewContent;
	
	
}