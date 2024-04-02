package com.kh.Final_Project.wishlist.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.wishlist.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {

	Page<WishList> findByCustomer_Ocid(Long ocid, Pageable pageable);

	/* */
	Optional<WishList> findByCustomerAndProduct(Customer customer, Product product);

	List<WishList> findByCustomer(Customer customer);

	void deleteByProduct(Product product);


}