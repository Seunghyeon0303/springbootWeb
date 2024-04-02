package com.kh.Final_Project.coupon.service;



import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;

import com.kh.Final_Project.coupon.entity.Coupon;

public interface CouponService {
	// 여기는 쿠폰
	public int count(Long ocid);
	public boolean loginCheck(String id);
	public Page<Coupon> page(Long ocid, int page);
	// 쿠폰 검색 기능
	Page<Coupon> findCouponCode(Long ocid,int page,String code);
	// 쿠폰 이 지난 쿠폰 내역인지 확인
	Page<Coupon> findYetCoupon(Long ocid,int page);
	// 쿠폰 정렬
	Page<Coupon> sortBy(Long memberOcid, String sort, int page);
	// 여기는 쿠폰 등록 관련
	int registerCoupon(String code, HttpSession session);
	Page<Coupon> modalPage(Long ocid, int page);

}