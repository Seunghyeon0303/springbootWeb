package com.kh.Final_Project.customer.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.kh.Final_Project.customer.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

	boolean existsByCustomerId(String customerId);

	Optional<Customer> findByCustomerId(String customerId);

	/* 관리자 페이지의 회원목록조회 (상단 검색창) AdminSeriveImp에서 사용 */
	// 이름 검색용
	Page<Customer> findByCustomerNameContaining(String name, Pageable pageable);

	// id 검색용
	Page<Customer> findByCustomerIdContaining(String id, Pageable pageable);

	// 이름 & id 검색용
	Page<Customer> findByCustomerNameContainingOrCustomerIdContaining(String titlekeyword, String contentkeyword,
			Pageable pageable);
	
	Page<Customer> findByCustomerIdNot(String customerId,Pageable pageable);

	Customer findByCustomerEmail(String email);
	
	
	
	

}
