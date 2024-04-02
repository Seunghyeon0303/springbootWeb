package com.kh.Final_Project.customerorder.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.customerorder.repository.CustomerOrderRepostiory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerOrderServiceImp implements CustomerOrderService {

	@Autowired
	private CustomerOrderRepostiory customerOrderRepostiory;

	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

	@Transactional
	public void saveOrderComment(String comment) {

	}

	/**
	 * 
	 * @return 비어있으면 0 둘중하나라도 안비어있으면 1
	 */
	public int checkOrderInfo(String postcode, String roadAddress, String customerAddress3, String phonePrefixValue,
			String phonePrefix, String phoneSuffix, String phonePrefixValue1, String phonePrefix1, String phoneSuffix1,
			HttpServletRequest request) {
		int check = 0;

		if ((phoneSuffix != null && !phoneSuffix.equals("")) && (phonePrefix != null && !phonePrefix.equals(""))) {
			check = 1;
		}
		if (check == 1 || (phoneSuffix1 != null && !phoneSuffix1.equals(""))
				&& (phonePrefix1 != null && !phonePrefix1.equals(""))) {
			check = 1;
		}
		// 세션에서 로그인된 고객 정보를 가져옵니다.
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		if (loggedInCustomer.getCustomerPhone() != null) {
			check = 1;
		}
		// 모든 필드가 비어있으면 0 반환
		if (loggedInCustomer.getCustomerAddress1() == null && (postcode == null || postcode.equals(""))) {
			return 0;
		}
		if (loggedInCustomer.getCustomerAddress2() == null && (roadAddress == null || roadAddress.equals(""))) {
			return 0;
		}
		if (loggedInCustomer.getCustomerAddress3() == null
				&& (customerAddress3 == null || customerAddress3.equals(""))) {
			return 0;
		}

		return check;
	}

	@Async
	public void updateCancel(String orderId) {
		// Runnable 태스크 정의
		Runnable task = () -> {
			Long id = Long.parseLong(orderId); // 문자열 orderId를 Long 타입으로 파싱
			CustomerOrder order = customerOrderRepostiory.findById(id).orElse(null);

			// 주문이 존재하고, "배송 대기중" 상태인 경우 상태를 "취소/환불"로 변경
			if (order != null) {
				order.setDeliveryStatus("취소/환불");
				customerOrderRepostiory.save(order);
			}
		};

		// Runnable 태스크를 2분 후에 실행하도록 스케줄링
		scheduler.schedule(task, 2, TimeUnit.MINUTES);
	}
	
	/**
	 * 주문이 배송 대기중인지 확인하는 메서드
	 * @param orderId 주문 ID
	 * @return 배송 대기중 상태이면 true, 아니면 false
	 */
	public boolean isStart(String orderId) {
	    Long id = Long.parseLong(orderId);
	    CustomerOrder order = customerOrderRepostiory.findById(id).orElse(null);
	    
	    // 주문이 존재하고, 상태가 "배송 대기중"이면 true 반환
	    if(order != null && order.getDeliveryStatus().equals("배송 대기중")) {
	        return true;
	    }
	    
	    // 주문이 존재하지 않거나 상태가 "배송 대기중"이 아니면 false 반환
	    return false;
	}
	

}