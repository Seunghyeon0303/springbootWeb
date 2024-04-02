package com.kh.Final_Project.review.controller;

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

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.review.entity.Review;
import com.kh.Final_Project.review.service.ReviewServiceImp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/review")
public class ReviewController {
	
	@Autowired
	private ReviewServiceImp reviewService;
	
	@GetMapping("/{productId}")
	public String review(HttpServletRequest request,Model model,@RequestParam(defaultValue="0")
	int page,@PathVariable(name="productId") Long productId) {
		
		log.info("productId : ",productId);
		log.info("ReviewController review()");
		HttpSession session = request.getSession(false);
		/*
		 * if (session == null || session.getAttribute("loggedInCustomer") == null) { //
		 * 로그인이 되어있지 않은 경우, 로그인 페이지로 리다이렉트 // RedirectAttributes를 사용하여 리다이렉트 시 메시지 전달
		 * return "redirect:/customer/login"; }
		 */
		// 저장되 있는 세션가져오기
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		model.addAttribute("loggedInCustomer", loggedInCustomer);
		
		// 세션에 있는 고객 고유 번호 뽑아내기 
		Long ocid = loggedInCustomer.getOcid();
		
		// 리뷰 총개수 가져오기
		int count = reviewService.countReview(ocid, productId);
		
		model.addAttribute("count", count);
		
		// 상세페이지에서 누른 아이디를 통해 그거에 맞춰서 가져오기 
		Page<Review> reviewList = reviewService.findReview(ocid, page, productId);
		
		// review 내용만 가져오기 
		model.addAttribute("reviewList", reviewList.getContent());
		// review 페이지 처리 가져오기
		model.addAttribute("reviewPage", reviewList);
		
		return "/product/review";
	}
}
