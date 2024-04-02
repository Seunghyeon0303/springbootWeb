package com.kh.Final_Project.customerorder.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customerorder.entity.CustomerOrder;

@Repository
public interface CustomerOrderRepostiory extends JpaRepository<CustomerOrder, Long> {

	/* admin컨트롤러에서 사용 */
	// 월매출
	@Query("SELECT SUM(c.price) FROM CustomerOrder c WHERE YEAR(c.orderDate) = :year AND MONTH(c.orderDate) = :month")
	Integer findTotalSalesByMonth(@Param("year") int year, @Param("month") int month);

	// 연매출
	@Query("SELECT SUM(c.price) FROM CustomerOrder c WHERE YEAR(c.orderDate) = :year")
	Integer findTotalSalesByYear(@Param("year") int year);

	// 일매출
	@Query(value = "SELECT CAST(order_date AS DATE) as orderDate, SUM(price) as dailySales " + "FROM customer_order "
			+ "GROUP BY CAST(order_date AS DATE) "
			+ "ORDER BY CAST(order_date AS DATE) DESC", countQuery = "SELECT COUNT(*) FROM (SELECT CAST(order_date AS DATE) FROM customer_order GROUP BY CAST(order_date AS DATE)) AS counts", nativeQuery = true)
	Page<Object[]> findDailySales(Pageable pageable);

	@Query(value = "SELECT * FROM customer_order ORDER BY order_id DESC", nativeQuery = true)
	List<CustomerOrder> findAllOrderByOrderIdDesc();

    List<CustomerOrder> findByCustomer(Customer customer);
	 
	 List<CustomerOrder> findByCustomerAndDeliveryStatusNot(Customer customer, String deliveryStatus);
	 List<CustomerOrder> findByCustomerAndDeliveryStatus(Customer customer, String string);

	List<CustomerOrder> findByDeliveryStatus(String deliveryStatus);
}