package com.kh.Final_Project.wishlist.service;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.wishlist.entity.WishList;

@Service
public interface WishListService {
	
	int addToWishList(String productIdStr, Customer customer);

	Page<WishList> getAllWishList(int page, HttpServletRequest request);
}