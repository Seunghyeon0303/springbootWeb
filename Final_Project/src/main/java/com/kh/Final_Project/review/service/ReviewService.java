package com.kh.Final_Project.review.service;

import org.springframework.data.domain.Page;

import com.kh.Final_Project.review.entity.Review;

public interface ReviewService {

	void saveReview(Review review);
	int countReview(Long ocid,Long productId);
	Page<Review> findReview(Long ocid, int page, Long productId);
}
