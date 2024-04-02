package com.kh.Final_Project.orderitem.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.coupon.service.CouponServiceImp;
import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customerorder.service.CustomerOrderServiceImp;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/order")
public class OrderItemRestController {

	@Autowired
	private CouponServiceImp couponService;

	@Autowired
	private CustomerOrderServiceImp customerOrderService;

	@PostMapping("/orderPay")
	@ResponseBody
	public Map<String, Object> orderPay(HttpServletRequest request, @RequestParam("totalPrice") String totalPrice) {
		Map<String, Object> response = new HashMap<>();
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loggedInCustomer") == null) {
			response.put("status", "redirect");
			response.put("location", "/customer/login");
			return response;
		}

		// 모든 처리가 성공적으로 끝났다면, 주문 확인 페이지로 리디렉션
		response.put("status", "success");
		response.put("location", "/order/orderPay");
		return response;
	}

	@GetMapping("/coupon/change")
	public Page<Coupon> couponChange(HttpServletRequest request,
			@RequestParam(defaultValue = "0", required = false) int page) {
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

		Long ocid = loggedInCustomer.getOcid();

		Page<Coupon> coupon = couponService.modalPage(ocid, page);

		return coupon;
	}

	@PostMapping("/discount")
	public ResponseEntity<?> applyDiscount(@RequestParam(defaultValue = "0") int discount) {
		return new ResponseEntity<>(String.format("%,d", discount), HttpStatus.OK);
	}

	// 배송 요청사항 저장해주기
	@PostMapping("/status")
	public ResponseEntity<String> orderStatus(@RequestParam(name = "comment") String comment) {

		customerOrderService.saveOrderComment(comment);

		return ResponseEntity.status(HttpStatus.OK).build();

	}

	// 유효성 검사해주기 아이디랑 주소
	@PostMapping("/check")
	@ResponseBody
	public ResponseEntity<?> orderCheck(@RequestParam(required = false) String postcode,
			@RequestParam(required = false) String roadAddress, @RequestParam(required = false) String customerAddress3,
			@RequestParam(required = false) String phonePrefixValue, @RequestParam(required = false) String phonePrefix,
			@RequestParam(required = false) String phoneSuffix,
			@RequestParam(required = false) String phonePrefixValue1,
			@RequestParam(required = false) String phonePrefix1, @RequestParam(required = false) String phoneSuffix1,
			HttpServletRequest request) {
		
		int check = customerOrderService.checkOrderInfo(postcode,roadAddress,customerAddress3,phonePrefixValue,phonePrefix,phoneSuffix
				,phonePrefixValue1,phonePrefix1,phoneSuffix1, request);
		if(check == 1) {
			return ResponseEntity.ok().body(1);
			
		}else  {
			return ResponseEntity.badRequest().body("주문 정보를 전부 기입해주세요.");
		}
	}

}
