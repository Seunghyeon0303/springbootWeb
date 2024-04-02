package com.kh.Final_Project.coupon.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.coupon.service.CouponServiceImp;
import com.kh.Final_Project.customer.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class CouponRestController {

	@Autowired
	private CouponServiceImp couponService;

	@PatchMapping("/register/coupon")
	public ResponseEntity<String> Coupon(@RequestParam("code") String code,HttpSession session) {
		// 클라이언트로부터 받은 쿠폰 코드를 이용하여 쿠폰 등록 작업 수행
		int result = couponService.registerCoupon(code,session);
		
		switch (result) {
		case 1:
			return ResponseEntity.ok("쿠폰이 정상적으로 등록되었습니다.");
		case 2:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("쿠폰이 만료 되었습니다.");
		case 3:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 사용한 쿠폰입니다");
		case 4:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효하지 않은 쿠폰번호입니다.");
		default:
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("쿠폰 등록에 실패했습니다.");
		}

	}

	@PostMapping("/coupon/sortBy")
	public ResponseEntity<Page<Coupon>> sortBy(Model model, @RequestParam("sort") String sort,
			@RequestParam(defaultValue = "0") int page, HttpServletRequest request) {

		// 세션에 저장된 객체 뽑아오기
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		model.addAttribute("loggedInCustomer", loggedInCustomer);

		// 고유 번호 뽑은 뒤 그 사람의 쿠폰 리스트 보여주기
		Long ocid = loggedInCustomer.getOcid();

		// 쿠폰 서비스에서 정렬된 쿠폰 목록과 함께 페이지 정보를 받아옴
		Page<Coupon> coupons = couponService.sortBy(ocid, sort, page);
		return new ResponseEntity<>(coupons, HttpStatus.OK);
	}

	// 쿠폰 검색하는거
	@PostMapping("/coupon/search")
	public ResponseEntity<Page<Coupon>> couponSearch(@RequestParam(defaultValue = "0") int page,
			HttpServletRequest request, @RequestParam(name = "input", required = false) String code) {
		// 세션
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		Long ocid = loggedInCustomer.getOcid();

		// 쿠폰 검색값 받아서 들어오기 만약 없다면 초기화면
		Page<Coupon> coupon = couponService.findCouponCode(ocid, page, code);
		if (coupon != null) {
			return new ResponseEntity<Page<Coupon>>(coupon, HttpStatus.OK);
		} else {
			return ResponseEntity.badRequest().build();
		}

	}

	// 쿠폰 체크 해주기 즉 지난 쿠폰
	@GetMapping("/coupon/check")
	public ResponseEntity<Page<Coupon>> couponCheck(@RequestParam(defaultValue = "0") int page,
			HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		Long ocid = loggedInCustomer.getOcid();

		Page<Coupon> coupon = couponService.findYetCoupon(ocid, page);

		return new ResponseEntity<>(coupon, HttpStatus.OK);

	}
}
