package com.kh.Final_Project.wishlist.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.Final_Project.wishlist.entity.WishList;
import com.kh.Final_Project.wishlist.service.WishListService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/wishlist")
public class WishListController {

	private final WishListService wishListService;

	@Autowired
	public WishListController(WishListService wishListService) {
		this.wishListService = wishListService;
	}


	// 찜목록 데이터 가져오기
	@GetMapping("/fragmentWishlist/{fragmentName}")
	public String getWishList(Model model,
		@PathVariable String fragmentName,
		@RequestParam(defaultValue = "0") int page,
		HttpServletRequest request) {
		
		log.info("위시리스트컨트롤러()의 getWishList() 요청성공");
		String fragmentWishlist = "customer/myPage/" + fragmentName;
		
		Page<WishList> wishList = wishListService.getAllWishList(page, request);
		
		long totalWishListSize = wishList.getTotalElements();
		
		// 모델에 데이터를 담아서 View로 전달
		model.addAttribute("wishList", wishList);
		model.addAttribute("totalWishListSize", totalWishListSize);
		
		log.info("찜 목록 데이터: {}", wishList);
		log.info("전체 찜 목록 데이터 개수: {}", totalWishListSize);
		
		return fragmentWishlist;
	}
	
	
	
	
	
	
	
	
	
}
