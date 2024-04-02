package com.kh.Final_Project.product.repository;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.entity.Product.Category;

public interface ProductRepository extends JpaRepository<Product, Long>{

	Page<Product> findByProductNameContaining(String productName, Pageable pageable);

	List<Product> findByCategory(Category category);

}
