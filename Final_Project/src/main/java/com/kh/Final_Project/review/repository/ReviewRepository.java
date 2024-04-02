package com.kh.Final_Project.review.repository;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.kh.Final_Project.review.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
	
	// 리뷰ㅜ 총개수
	 @Query("SELECT COUNT(r) FROM Review r WHERE r.customer.ocid = :ocid AND r.product.productID = :productId")
	int CountByCustomerOcidAndProductId(Long ocid,Long productId);
	
    // 고객 고유 번호와 상품 아이디로 리뷰를 찾는 메서드
	Page<Review> findByCustomerOcidAndProductProductID(Long ocid, Pageable pageable, Long productId);
}

