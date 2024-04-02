package com.kh.Final_Project.review.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.product.entity.Product;

import lombok.Data;

@Data
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long reviewId;
	
	@ManyToOne
	@JoinColumn(name = "product_id") // Product 테이블의 productID 컬럼과 매핑
	private Product product;
	
	@ManyToOne
	@JoinColumn(name = "customer_ocid") // Product 테이블의 productID 컬럼과 매핑
	private Customer customer;
	
	@Column
	private double rating;
	
	@Column
	private String reviewContent;
	
	@Column
	private int recommend;
	
	@Column
	private LocalDateTime reviewTime;
	
}