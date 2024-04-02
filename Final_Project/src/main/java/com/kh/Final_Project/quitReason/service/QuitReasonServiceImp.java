package com.kh.Final_Project.quitReason.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.repository.CustomerRepository;
import com.kh.Final_Project.quitReason.entity.QuitReason;
import com.kh.Final_Project.quitReason.repository.QuitReasonRepostiory;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class QuitReasonServiceImp implements QuitReasonService {

	@Autowired
	private QuitReasonRepostiory quitReasonRepostiory;
	
	@Autowired
	private CustomerRepository customerRepository;

	public void insert(Map<String, String> reasonMap, Customer loggedInCustomer,HttpSession session) {
	    String reasonTypeStr = reasonMap.get("reason"); // 탈퇴 사유 타입
	    String reasonWrite = reasonMap.get("quitReason"); // 사용자가 입력한 탈퇴 사유
	    int reasonTypeInt = 0;
	    Long ocid = loggedInCustomer.getOcid();
	    
	    try {
	        reasonTypeInt = Integer.parseInt(reasonTypeStr);
	    } catch (NumberFormatException e) {
	        // 로그 출력 또는 적절한 예외 처리
	        log.error("Reason type parsing error: {}", e.getMessage());
	        // 예외 처리 후, 이 메서드의 실행을 중단하거나 기본값 사용 등의 로직 추가
	    }

	    QuitReason entity;

	    if (reasonTypeInt == 8) {
	        // 사용자가 직접 입력한 탈퇴 사유 처리
	        entity = QuitReason.builder()
	                .reasonType(String.valueOf(reasonTypeInt))
	                .reason(reasonWrite)
	                .build();
	    } else {
	        // 고정된 탈퇴 사유 처리
	        entity = QuitReason.builder()
	                .reasonType(String.valueOf(reasonTypeInt))
	                .build();
	    }

	    quitReasonRepostiory.save(entity);
	    customerRepository.deleteById(ocid);
	    session.invalidate();
	    log.info("삭제 성공");
	}


}
