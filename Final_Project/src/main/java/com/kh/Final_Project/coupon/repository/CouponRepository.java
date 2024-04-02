package com.kh.Final_Project.coupon.repository;



import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.kh.Final_Project.coupon.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
	
	// 쿠폰에서 커스토머 에있는 ocid와 쿠폰 상태가 1인 경우에만 가져오기 아 미친 여기에도 날짜 구별해서 넣어야되네
	@Query("SELECT c FROM Coupon c WHERE c.customer.ocid = :ocid AND c.status = 1 AND c.deadlineDate > CURRENT_TIMESTAMP")
	Page<Coupon> findActiveCouponsByCustomerOcid(Long ocid, Pageable pageable);

	// 쿠폰 ocid에 맞는 개수 전체
	@Query("select COUNT(c) from Coupon c where c.customer.ocid = :ocid And c.status = 1 AND c.deadlineDate > CURRENT_TIMESTAMP")
	int countActiveCouponsByCustomerOcid(Long ocid);
	// 쿠폰 최근 등록순으로 나열하기
	@Query("SELECT c FROM Coupon c WHERE c.customer.ocid = :ocid AND c.status = 1 AND c.deadlineDate > CURRENT_TIMESTAMP ORDER BY ABS(FUNCTION('DATEDIFF', CURRENT_TIMESTAMP(), c.deadlineDate)) ASC")
	Page<Coupon> findActiveCouponsOrderByDeadlineDateAsc(Long ocid, Pageable pageable);
	 
	// 쿠폰 지난 것들 보여주기
    @Query("SELECT c FROM Coupon c WHERE c.customer.ocid = :ocid AND c.deadlineDate < CURRENT_TIMESTAMP")
    Page<Coupon> findByCustomerOcidAndExpired(Long ocid,Pageable pageable);
    
    // 쿠폰 검색해서 있다면 보여주기
//    Containing code가 포함한걸 
    // 거기에 상태값 플러스 아이디 비교해서 맞는거만  안됨 슈발
    @Query("SELECT c FROM Coupon c WHERE c.customer.ocid = :ocid AND c.status = 1 AND c.deadlineDate > CURRENT_TIMESTAMP And c.code LIKE %:code%")
    Page<Coupon> findActiveCouponsByCustomerOcidAndCodeContaining(@Param("code") String code, Pageable pageable, @Param("ocid") Long ocid);

    // 쿠폰 코드로 확인
	Optional<Coupon> findByCode(String code);
    
	/* 관리자 페이지의 쿠폰조회 (상단 검색창) AdminSeriveImp에서 사용*/
	// 쿠폰 코드 검색용
	Page<Coupon> findByCodeContaining(String keyword, Pageable pageable);

	Optional<Coupon> findByCodeAndCustomerOcid(String code,Long ocid);
    
}