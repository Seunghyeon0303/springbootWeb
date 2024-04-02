package com.kh.Final_Project.coupon.vo;

import java.time.LocalDateTime;

import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.customer.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CouponForm {
	
	private Long ocid;
	private Customer customer;
	private String code;
	private String message;
	private int discount;
	private LocalDateTime deadlineDate;
	private int status;
	public Coupon toEntity() {
		return new Coupon(ocid,customer, code, message, discount, deadlineDate, status);
	}
	
}