package com.kh.Final_Project.orderitem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.orderitem.entity.OrderItem;
import com.kh.Final_Project.product.entity.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{
	
	
	List<OrderItem> findByCustomerOrder(CustomerOrder customerOrder);

	void deleteByProduct(Product product);

}
