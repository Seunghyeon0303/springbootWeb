package com.kh.Final_Project.quitReason.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.kh.Final_Project.customer.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class QuitReason {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Ocid;
	
	@Column(name = "reason_type")
	private String reasonType;
	
	@Column(name = "reason")
	private String reason;
	
}
