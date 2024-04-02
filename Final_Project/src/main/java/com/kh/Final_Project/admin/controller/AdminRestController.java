package com.kh.Final_Project.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminRestController {
	
	@Autowired
	private ProductService productService;
	
	@PostMapping("/adminItemDelete")
	public ResponseEntity<?> adminItemDelete(@RequestBody String productID) {
		
		Long id  = Long.parseLong(productID);
		
		if(!productService.deleteById(id)) {
			return ResponseEntity.badRequest().build();
		}
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}

	
	
	
	
}
