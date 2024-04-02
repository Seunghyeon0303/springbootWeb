package com.kh.Final_Project.customer.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.orderitem.entity.Pocket;
import com.kh.Final_Project.review.entity.Review;
import com.kh.Final_Project.wishlist.entity.WishList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Customer {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long ocid;
    
    @NonNull
    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "customer_pw")
    private String customerPw;

    @NonNull
    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone")
    private String customerPhone;
    
    @NonNull
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "customer_address1")
    private String customerAddress1;
    
    @Column(name = "customer_address2")
    private String customerAddress2;
    
    @Column(name = "customer_address3")
    private String customerAddress3;

    @Column(name = "customer_ship")
    private String customerShip;
    
    @JsonManagedReference
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> coupons = new ArrayList<>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerOrder> customerOrders = new ArrayList<>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pocket> pockets = new ArrayList<>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> reviews = new ArrayList<>();
    
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<WishList> wishLists = new ArrayList<>();
    
	public Customer(@NonNull String customerId, String customerPw, @NonNull String customerEmail, String customerPhone,
			@NonNull String customerName, String customerAddress1, String customerAddress2, String customerAddress3) {
		super();
		this.customerId = customerId;
		this.customerPw = customerPw;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
		this.customerName = customerName;
		this.customerAddress1 = customerAddress1;
		this.customerAddress2 = customerAddress2;
		this.customerAddress3 = customerAddress3;
	}
    
    
    
}