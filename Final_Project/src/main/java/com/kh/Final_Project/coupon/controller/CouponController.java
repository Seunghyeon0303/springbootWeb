package com.kh.Final_Project.coupon.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.coupon.service.CouponServiceImp;
import com.kh.Final_Project.customer.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/coupon")
public class CouponController {
	@Autowired
	private CouponServiceImp couponService;

	@GetMapping("/fragmentCoupon/{fragmentName}")
	public String getFragmentCoupon(@PathVariable String fragmentName, Model model, HttpServletRequest request,
		 @RequestParam(defaultValue = "0") int page) {
		String fragmentCoupon = "customer/myPage/" + fragmentName;

		// 세션에 저장된 객체 뽑아오기
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		model.addAttribute("loggedInCustomer", loggedInCustomer);

		// 객체에 있는 이름 뽑아주기
		String name = loggedInCustomer.getCustomerName();
		model.addAttribute("name", name);

		// 고유 번호 뽑은뒤 그사람의 쿠폰 리스트 보여주기
		Long memberOcid = loggedInCustomer.getOcid();

		// 갯수 뽑기
		int count = couponService.count(memberOcid);
		model.addAttribute("count", count);

		/* 목록조회 */
		Page<Coupon> couponList = couponService.page(memberOcid, page);
		model.addAttribute("coupon", couponList.getContent());
		
		// 페이지
		model.addAttribute("page",couponList);
		return fragmentCoupon;
	}
	


}