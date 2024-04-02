package com.kh.Final_Project.orderitem.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/payment")
@RestController
public class KakaoRestController {
	  /**
     * 결제 진행 중 취소
     */
	@GetMapping("/cancel")
	public ResponseEntity<String> cancel() {
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카카오페이 결제에 실패했습니다 <br> 다시 시도해주세요");
	}
	
}
