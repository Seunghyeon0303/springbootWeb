package com.kh.Final_Project.coupon.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.coupon.repository.CouponRepository;
import com.kh.Final_Project.customer.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CouponServiceImp implements CouponService {

	// 쿠폰
	@Autowired
	private CouponRepository couponRepository;

	// 다써야 되니까 전역 변수로 선언
	int pageSize = 3;
	Pageable pageable;

	@Override
	public boolean loginCheck(String id) {
		return false;
	}

	@Override
	public Page<Coupon> page(Long ocid, int page) {
		// 페이지 처리
		if(page < 0) {
			page = 0;
		}

		pageable = PageRequest.of(page, pageSize);

		return couponRepository.findActiveCouponsByCustomerOcid(ocid, pageable);
	}
	
	@Override
	public Page<Coupon> modalPage(Long ocid, int page) {
		// 페이지 처리
		if(page < 0) {
			page = 0;
		}

		pageable = PageRequest.of(page, 10);

		return couponRepository.findActiveCouponsByCustomerOcid(ocid, pageable);
	}

	@Override
	// 쿠폰 전체 개수 활성화 된 애들만
	public int count(Long ocid) {

		return couponRepository.countActiveCouponsByCustomerOcid(ocid);

	}

	// 쿠폰 정렬
	@Override
	public Page<Coupon> sortBy(Long ocid, String sort, int page) {
		// 페이지 처리
		pageable = PageRequest.of(page, pageSize);

		if (sort.equals("deadline")) {
			return couponRepository.findActiveCouponsOrderByDeadlineDateAsc(ocid, pageable);
		} else {
			return couponRepository.findActiveCouponsByCustomerOcid(ocid, pageable);
		}

	}

	// 검색 기능 쿠폰
	@Override
	public Page<Coupon> findCouponCode(Long ocid, int page, String code) {
		pageable = PageRequest.of(page, pageSize);
		// 쿠폰 코드로 쿠폰을 찾아봅니다. 먼저 쿠폰이 있는지 확인하기
		Page<Coupon> coupons = couponRepository.findActiveCouponsByCustomerOcidAndCodeContaining(code, pageable, ocid);
		log.info("coupon {}", coupons);
		if (coupons.isEmpty()) {
			return null;
		}
		return coupons;

	}

	// 지난 기한 보여주는 쿠폰
	@Override
	public Page<Coupon> findYetCoupon(Long ocid, int page) {
		pageable = PageRequest.of(page, pageSize);
		
		Page<Coupon> yetPageList = couponRepository.findByCustomerOcidAndExpired(ocid, pageable);
		if (yetPageList.hasContent()) {
			return yetPageList;
		} else {
			return Page.empty(); // 빈 페이지 반환
		}
	}

	// ------------------------쿠폰 등록-------------------
	/**
	 * @return 등록 성공 = 1 , 시간 지남 = 2, 이미 등록 3, 없는 번호 = 4
	 */
	@Override
	public int registerCoupon(String code,HttpSession session) {
		
		Customer a = (Customer) session.getAttribute("loggedInCustomer");
		Long ocid = a.getOcid();
		// 쿠폰 코드로 쿠폰을 찾아봅니다.
		
		Optional<Coupon> couponOpt = couponRepository.findByCodeAndCustomerOcid(code,ocid);
		
		if (couponOpt.isEmpty()) // 쿠폰이 존재하지 않으면
			return 4;
		
		Coupon coupon = couponOpt.get(); // Opt에서 쿠폰 객체 꺼내기
		
		if(coupon.getStatus() != 0)
			return 3;
		
		// 쿠폰이 존재하고 상태가 유효한 경우
		if (coupon.getDeadlineDate().isAfter(LocalDateTime.now())) {
			coupon.setStatus(1);
			couponRepository.save(coupon);
			return 1; // 쿠폰 등록 성공
		}else {
			return 2;
		}
		

	}

}
