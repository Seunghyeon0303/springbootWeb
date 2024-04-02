package com.kh.Final_Project.wishlist.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class WishList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long wishlistId;
	
	@ManyToOne
	@JoinColumn(name = "product_id") // Product 테이블의 productId 컬럼과 매핑
	private Product product;

	@ManyToOne
	@JoinColumn(name = "ocid") // Customer 테이블의 customerOcid 컬럼과 매핑
	private Customer customer;

	public WishList(Customer customer, Product product) {
		this.customer = customer;
		this.product = product;
	}

}