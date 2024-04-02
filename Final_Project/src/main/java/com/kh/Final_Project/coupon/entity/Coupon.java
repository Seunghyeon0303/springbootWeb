package com.kh.Final_Project.coupon.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.kh.Final_Project.customer.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Data
@Entity
public class Coupon {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ocid;
	
    @ManyToOne
    @JoinColumn(name = "customer_ocid")
    @JsonBackReference
	private Customer customer;
	
	@NonNull
	private String code;
	
	@Column
	@NonNull
	private String message;
	@Column
	private int discount;
	@Column
	private LocalDateTime deadlineDate;
	
	@Column
	private int status; // 쿠폰 상태값 추가 등록 떄문에  1이 이미 등록한 상태 0이 미등록
}