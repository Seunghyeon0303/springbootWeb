package com.kh.Final_Project.customer.vo;


import com.kh.Final_Project.customer.entity.Customer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerForm {

    private String customerId;
    private String customerPw;
    private String customerEmail;
    private String customerPhone;
    private String customerName;
    private String customerAddress1;
    private String customerAddress2;
    private String customerAddress3;
    
    public Customer toEntity() {
    	return new Customer(customerId, customerPw, customerEmail, customerPhone,
    			customerName, customerAddress1, customerAddress2, customerAddress3);
    }
    
    public Customer toKaKaoEntity() {
    	return new Customer(customerId, customerEmail,customerName);
    }
    
    
}
