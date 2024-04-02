package com.kh.Final_Project.customer.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.Final_Project.admin.service.AdminService;
import com.kh.Final_Project.admin.service.AdminServiceImp;
import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.service.CustomerServiceImp;
import com.kh.Final_Project.customer.service.KakaoService;
import com.kh.Final_Project.customer.service.NaverService;
import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.orderitem.repository.OrderItemRepository;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/customer")
public class CustomerController {

	@Autowired
	private CustomerServiceImp service;

	@Autowired
	private KakaoService kakaoService;

	@Autowired
	private NaverService naverService;

	@Autowired
	private AdminServiceImp adminService;

	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
		model.addAttribute("naverUrl", naverService.getNaverLogin());
		return "customer/login/login";
	}

	@GetMapping("/signUpType")
	public String signUpType(Model model) {
		model.addAttribute("kakaoUrl", kakaoService.getKakaoLogin());
		model.addAttribute("naverUrl", naverService.getNaverLogin());
		return "customer/login/signUpType";
	}

	@GetMapping("/signUpAgree")
	public String signUpAgree(Model model) {
		return "customer/login/signUpAgree";
	}

	@GetMapping("/signUp")
	public String signUp(Model model) {
		return "customer/login/signUp";
	}

	@GetMapping("/myPage")
	public String myPage(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false); // 기존 세션 가져오기, 없으면 null 반환
		if (session == null || session.getAttribute("loggedInCustomer") == null) {
			// 로그인이 되어있지 않은 경우, 로그인 페이지로 리다이렉트
			// RedirectAttributes를 사용하여 리다이렉트 시 메시지 전달
			return "redirect:/customer/login";
		}
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

		model.addAttribute("loggedInCustomer", loggedInCustomer);

		return "customer/myPage/myPage"; // 로그인이 되어있는 경우, 마이페이지로 이동
	}

	@GetMapping("/fragment/{fragmentName}")
	public String getFragment(@PathVariable String fragmentName, Model model, HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		boolean oauthLogin; // oauth로그인인지 맞다면 true

		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

		if (loggedInCustomer.getCustomerPw().contains("oauthAPI로그인")) { // oauth 로그인 시 비밀번호 변경 버튼 지우기
			oauthLogin = true;
			model.addAttribute("isPasswordNull", true);
		} else {
			oauthLogin = false;
			model.addAttribute("isPasswordNull", false);
		}

		if (fragmentName.equals("pwCheck")) { // 만약 oauth 로그인시 패스워드 확인이 불가하기 때문에 개인정보 수정 페이지 클릭시 바로 이동(비밀번호 체크 패스)
			if (oauthLogin) {
				fragmentName = "myInfoRe";
			}
		}

		if (fragmentName.equals("orderDelivery")) {

			List<CustomerOrder> customerOrders = service.deliveryStatus(loggedInCustomer);

			model.addAttribute("customerOrders", customerOrders);
			model.addAttribute("customerOrderSize", customerOrders.size());
			model.addAttribute("customer", loggedInCustomer);
		}

		if (fragmentName.equals("cancel")) {
			List<CustomerOrder> customerOrders = service.deliveryStatusNot(loggedInCustomer);
			model.addAttribute("customerOrders", customerOrders);
			model.addAttribute("customerOrderSize", customerOrders.size());
			model.addAttribute("customer", loggedInCustomer);
		}

		String fragment = "customer/myPage/" + fragmentName;
		model.addAttribute("loggedInCustomer", loggedInCustomer);

		return fragment;
	}

	@PostMapping("/login")
	public String performLogin(@RequestParam("customerId") String id, @RequestParam("customerPw") String password,
			HttpServletRequest request, HttpSession session) {

		boolean login = service.login(id, password, request);

		// 인증 성공 시, 사용자 정보를 세션에 저장
		if (login) {

			if (adminService.isAdmin(session)) {
				return "redirect:/admin/";
			}

			return "redirect:/"; // 로그인 성공 시 리다이렉트할 페이지
		} else {
			return "customer/login/login"; // 로그인 실패 시, 로그인 페이지로 다시 리다이렉트
		}
	}

	@GetMapping("/logout")
	public String performLogout(HttpServletRequest request) {
		service.logout(request);
		return "redirect:/"; // 로그아웃 후 리다이렉트할 페이지
	}

	@GetMapping("/findId")
	public String adminCouponRegi(HttpSession session) {
		return "customer/login/findId";
	}

	// 비밀번호 변경 관련
	@GetMapping("/changePassword")
	public String changePassword(HttpSession session) {
		return "/customer/login/changePassword";
	}

}
