package com.kh.Final_Project.customer.service;

import javax.servlet.http.HttpServletRequest;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.vo.CustomerForm;

public interface CustomerService {
	
	public Customer signUp(CustomerForm customerForm);
	
	
	public boolean idDuplicateCheck(String id);
	
	public boolean login(String customerId, String customerPw, HttpServletRequest request);

	public Customer kakaoSignUp(CustomerForm customerForm, HttpServletRequest request);
	public Customer naverSignUp(CustomerForm customerForm, HttpServletRequest request);
	
	public void insertAdminId();
	
	Customer findCustomerById(Long customerId);

}
