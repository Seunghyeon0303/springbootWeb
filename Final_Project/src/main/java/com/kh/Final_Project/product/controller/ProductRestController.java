package com.kh.Final_Project.product.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/product")
@Slf4j
public class ProductRestController {
	
	@Autowired
	private ProductService productService;

    @PostMapping("/checkStock")
    public ResponseEntity<?> checkStock(@RequestBody List<Map<String, Object>> productQuantities) {
    	log.info("재고 확인 요청 성공");
        // 재고 확인 로직 구현
    	
    	List<Product> list = productService.checkStock(productQuantities);
    	
    	if(list == null) {
    		return ResponseEntity.ok().body(Map.of("isStockAvailable",false,"result","현재 등록되지 않은 상품이 있습니다. 장바구니를 다시 담아주세요."));
    	}
    	// 모든 상품의 재고가 충분하다고 가정하고 응답 반환
    	if(list.size()==0) {
    		return ResponseEntity.ok().body(Map.of("isStockAvailable", true));
    	}
    	StringBuilder sb = new StringBuilder();
    	sb.append("현재 ");
    	for(int i = 0; i < list.size(); i++) {
    		Product product = list.get(i);
    		sb.append(product.getProductName());
    		if(i < list.size()-1) {
    			sb.append(", ");
    		}
    	}
    	sb.append("의 재고가 부족합니다. 나중에 다시 시도해주세요.");
    	
    	return ResponseEntity.ok().body(Map.of("isStockAvailable",false,"result",sb.toString()));

    }
	
	

}
