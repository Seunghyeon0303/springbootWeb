package com.kh.Final_Project.orderitem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.orderitem.entity.Pocket;
import com.kh.Final_Project.orderitem.repository.OrderItemRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OrderItemServiceImp implements OrderItemService {
	
	@Autowired
	private OrderItemRepository orderRepository;
	
	@Override
	// 들어온 개수되로 가격 곱해서 보여주기
	public String getPrice(List<Pocket> pocketList) {
		int totalPrice = 0;
		for(Pocket pocket : pocketList) {
			// 상품이 가지고 있는 가격
			int productPrice = Integer.parseInt(pocket.getProduct().getPrice().replace(",",""));
			//  장바구니가 가지고 있는 개수
			int pocketCount = pocket.getCount();
			
			 totalPrice += productPrice * pocketCount; // 상품 가격 * 장바구니 개수
		}
		log.info("totalPrice : {}",totalPrice );
		
		// 다시 쉼표로 보여야되서 변환
		return String.format("%,d", totalPrice);
	}

	@Override
	// 들어온 개수 보여주기
	public int getCount(List<Pocket> pocketList) {
		int totalCount = 0;
		for(Pocket pocket : pocketList) {
			//  장바구니가 가지고 있는 개수
			int pocketCount = pocket.getCount();
			
			totalCount += pocketCount; 
		}
		log.info("totalCount : {}",totalCount );
		
		// 다시 쉼표로 보여야되서 변환
		return totalCount;
	}
	
	@Override
	// 들어온 상품명 보여주기
	public StringBuffer getProductName(List<Pocket> pocketList) {
		StringBuffer productNameAll = new StringBuffer();
		for (int i = 0; i < pocketList.size(); i++) {
	        Pocket pocket = pocketList.get(i);
	        String productName = pocket.getProduct().getProductName();
	        productNameAll.append(productName);
	        // 이러면 한번 반복문으로 이름 가져올때 쉼표를 추가함  근데 마지막 요소면 쉼표 추가안하기
	        if (i < pocketList.size() - 1) { 
	            productNameAll.append(", ");
	        }
	    }
		
		log.info("productNameAll : {}",productNameAll );
		return productNameAll;
		
	}
}
