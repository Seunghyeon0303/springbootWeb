package com.kh.Final_Project.orderitem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.orderitem.entity.Pocket;
import com.kh.Final_Project.orderitem.service.OrderItemServiceImp;
import com.kh.Final_Project.orderitem.service.PocketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/order")
public class OrderItemController {

	@Autowired
	private PocketService pocketService;

	@Autowired
	private OrderItemServiceImp orderItemService;

	@GetMapping("/orderitem")
	public String orderItem(HttpServletRequest request, Model model) {
		HttpSession session = request.getSession(false); // 기존 세션 가져오기, 없으면 null 반환
		if (session == null || session.getAttribute("loggedInCustomer") == null) {
			// 로그인이 되어있지 않은 경우, 로그인 페이지로 리다이렉트
			// RedirectAttributes를 사용하여 리다이렉트 시 메시지 전달
			return "redirect:/customer/login";
		}

		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

		model.addAttribute("loggedInCustomer", loggedInCustomer);

		// 고유 번호 뽑은뒤 그사람의 장바구니 리스트 보여주기
		Long ocid = loggedInCustomer.getOcid();

		// 장바구니 총 개수 출력
		int count = pocketService.pocketCount(ocid);
		model.addAttribute("count", count);

		// 장바구니 리스트 출력
		List<Pocket> pocketList = pocketService.pocketList(ocid);
		model.addAttribute("pocketList", pocketList);

		return "/customer/pocket/pocket";
	}

	// 주문 페이지로 가기
	@PostMapping("/orderPay")
	public String orderPay(HttpServletRequest request, Model model, @RequestParam List<Long> productId,
			@RequestParam List<String> count, @RequestParam List<Boolean> check, @RequestParam List<String> priceAll) {
		// pockets 정보를 기반으로 비즈니스 로직 수행
		// 예: pockets.forEach(pocketDto -> ...);ㅇ

		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("loggedInCustomer") == null) {
			return "redirect:/customer/login";
		}
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		model.addAttribute("loggedInCustomer", loggedInCustomer);

		List<Pocket> pocketList = pocketService.OrderPocket(productId, count, check, priceAll, request);

		model.addAttribute("pocketList", pocketList);
		
		// 상품 총 가격 갯수 곱해서 보여주기
		String allPrice = orderItemService.getPrice(pocketList);

		model.addAttribute("allPrice", allPrice);
		
		// 상품 총 갯수 보여주기
		int productCount = orderItemService.getCount(pocketList);
		
		model.addAttribute("productCount", productCount);
		
		// 상품 이름 보내주기 
		StringBuffer productName = orderItemService.getProductName(pocketList);
		
		model.addAttribute("productName", productName);
		
		return "/customer/pocket/orderitemPay";
	}



}