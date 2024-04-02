package com.kh.Final_Project.orderitem.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;
    
    @NonNull
    @ManyToOne
    @JoinColumn(name = "order_id") // CustomerOrder 엔티티의 orderId 필드에 매핑되는 외래키
    private CustomerOrder customerOrder; // CustomerOrder 엔티티 참조
    
    @NonNull
    @OneToOne
    @JoinColumn(name = "product_id") // Product 엔티티의 productID 필드에 매핑되는 외래키
    private Product product; // Product 엔티티 참조
    
    @Column(name = "price")
    private int price;
    
    @Column(name = "count")
    private int count;
}