package com.kh.Final_Project.admin.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.kh.Final_Project.util.AdminDailySales;

public interface AdminService {
	
	int calcMonthly(int year, int month);
	int calcYearly(int year);
	Page<AdminDailySales> getDailySales(Pageable pageable);
	long countAllCustomers();
	long countAllNoti();
	long countAllFaq();
	long countAllEvent();
}
