package com.kh.Final_Project.orderitem.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.orderitem.service.KakaoPayService;
import com.kh.Final_Project.orderitem.vo.Kakao;

import lombok.extern.slf4j.Slf4j;

@RequestMapping("/payment")
@Controller
@Slf4j
public class KakaoController {

	@Autowired
	private KakaoPayService kakaoPayService;

	/**
	 * 결제요청
	 */
	@GetMapping("/kakaoPay")
	public String kakaoPay(Model model, HttpServletRequest request, @RequestParam(name = "count") int count,
			@RequestParam(name = "totalPrice") int totalPrice, @RequestParam(name = "productName") String productName,
			String recipient, String postcode, String roadAddress, String customerAddress3, String phonePrefix,
			String phoneSuffix, String phonePrefix1, String phoneSuffix1, String phonePrefixValue,
			String phonePrefixValue1) {

		return kakaoPayService.KakaoPayReady(request, count, totalPrice, productName, recipient, postcode, roadAddress,
				customerAddress3, phonePrefix, phoneSuffix, phonePrefix1, phoneSuffix1, phonePrefixValue,
				phonePrefixValue1);
	}

	@GetMapping("/success")
	public String success(Model model, String pg_token, HttpSession session) {
	    
	    Kakao app = kakaoPayService.KakaoApprove(pg_token,session);
	    
	    model.addAttribute("info", app);
	    
	    return "customer/pocket/kakao";
	}

}
