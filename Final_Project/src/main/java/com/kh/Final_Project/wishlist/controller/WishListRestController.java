package com.kh.Final_Project.wishlist.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.wishlist.service.WishListServiceImp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/wishlist")
public class WishListRestController {
	
	@Autowired
	private WishListServiceImp wishListService;
	
	
	
	@PostMapping("/deleteWishList")
	public ResponseEntity<?> deleteWishList(@RequestBody Map<String, String> map,HttpSession session){
		
		String productIdStr = map.get("productId");
		
		Long productId = Long.parseLong(productIdStr);
		
		if(wishListService.deleteWishList(session, productId)) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	
	
	

}
