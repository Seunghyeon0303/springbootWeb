package com.kh.Final_Project.customerorder.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.orderitem.entity.OrderItem;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "customer_ocid") // 이전에 ocid로 지정되었으나, customer 테이블의 PK와 일치하도록 변경
    private Customer customer;

    @Column(name = "delivery_status")
    private String deliveryStatus;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Column(name = "delivery_cost")
    private String deliveryCost;
    
    @NonNull
    @Column(name = "customer_order_name")
    private String customerOrderName;
    
    @Column(name = "customer_order_address1")
    private String customerOrderAddress1;
    
    @Column(name = "customer_order_address2")
    private String customerOrderAddress2;
    
    @Column(name = "customer_order_address3")
    private String customerOrderAddress3;

    @Column(name = "customer_order_phone")
    private String customerOrderPhone;
    
    @Column(name = "count")
    private int count;
    
    @Column(name = "price")
    private int price;
    
    @Column(name = "customer_comment")
    private String comment;
    
    @OneToMany(mappedBy = "customerOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
    
}