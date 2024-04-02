package com.kh.Final_Project.orderitem.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.orderitem.entity.Pocket;
import com.kh.Final_Project.product.entity.Product;
public interface PocketRepository extends JpaRepository<Pocket, Long> {
	
	int countByCustomerOcid(Long ocid);
	
	// 사용자 고유 번호 가지고 와서 그거에 해당하는 Pocket 찾아오기
	List<Pocket> findByCustomerOcid(Long ocid);
	
	// 눌렀을떄 한 상품만 없애기게끔 할려고 
	void deleteByCustomerOcid(Long ocid);
	// 전체 상품 삭제 
	void deleteAllByCustomerOcid(Long ocid);
	
	// 여기는 레스트 컨트롤러 
	// 눌렀을떄 한 상품만 없애기게끔 할려고 
	void deleteByProductProductIDAndCustomerOcid(Long productID, Long ocid);
	
	Pocket findByCustomerAndProduct(Customer customer, Product product);

	void deleteByProduct(Product product);
	
	// 여기는 레스트 컨트롤러 
	
}
