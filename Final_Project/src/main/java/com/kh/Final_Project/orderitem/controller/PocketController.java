package com.kh.Final_Project.orderitem.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.orderitem.service.PocketService;
import com.kh.Final_Project.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
@RequestMapping("/api/order")
public class PocketController {
	
	@Autowired
	ProductService productService;
	
	@Autowired
	PocketService pocketService;
	
	@PostMapping("/orderitem")
	public ResponseEntity<?> addPocket(@RequestBody Map<String, Object> data,HttpServletRequest request,Model model){
		   
		    HttpSession session = request.getSession(false); // 기존 세션 가져오기, 없으면 null 반환
		    if (session == null || session.getAttribute("loggedInCustomer") == null) {
		      
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 후 이용가능합니다");
		    }
		    
		    boolean result = pocketService.addPocket(data,request);
		 
	        if(result) {
	        	
	        	return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
	        }
		    
	        return ResponseEntity.badRequest().build();

	}
	
	@PostMapping("/addToCart")
	public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> data,HttpServletRequest request,Model model){
		   
		    HttpSession session = request.getSession(false); // 기존 세션 가져오기, 없으면 null 반환
		    if (session == null || session.getAttribute("loggedInCustomer") == null) {
		      
		        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 후 이용가능합니다");
		    }
		    
		    boolean result = pocketService.addToCart(data,request);
		 
	        if(result) {
	        	
	        	return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");
	        }
		    
	        return ResponseEntity.badRequest().build();

	}
	
}
