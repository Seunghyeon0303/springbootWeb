package com.kh.Final_Project.review.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.review.entity.Review;
import com.kh.Final_Project.review.repository.ReviewRepository;


@Service
public class ReviewServiceImp implements ReviewService {
	
	int pageSize = 5;
	
	Pageable pageable;
	
	@Autowired
	private ReviewRepository reviewRepository;

	@Override
	public void saveReview(Review review) {
		reviewRepository.save(review);
	}
	
	// 리뷰 총개수 받기
	@Override
	public int countReview(Long ocid,Long productId) {
		return reviewRepository.CountByCustomerOcidAndProductId(ocid, productId);
	}
	
	
	// 리뷰 고객고유번호 먼저 받고 레파지토리를 통해 db로 
	@Override
	public Page<Review> findReview(Long ocid,int page,Long productId) {
		
		pageable = PageRequest.of(page, pageSize);
		
		return reviewRepository.findByCustomerOcidAndProductProductID(ocid,pageable, productId);
	}

	
}