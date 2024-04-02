package com.kh.Final_Project.orderitem.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.orderitem.service.PocketService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/pocket")
public class PocketRestController {
	
	@Autowired
	private PocketService pocketService;
	
	@GetMapping("/delete")
	public ResponseEntity<?> deletePocket(@RequestParam(name="orderItemId", required = false) Long productId,
			HttpServletRequest request) {
	    
	    HttpSession session = request.getSession(false);
	    Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
	    
	    // 고객에 대한 번호 뽑기
	    Long ocid = loggedInCustomer.getOcid();
	    pocketService.pocketDeleteByProductID(productId,ocid);
	    return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	
	// 드롭다운 변경시 개수 변경
	@PostMapping("/countChange")
	public ResponseEntity<?> countChange(@RequestParam int changeCount,@RequestParam(name = "pocketId") Long pocketId){
		
		pocketService.countChange(changeCount,pocketId);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}
}
