package com.kh.Final_Project.orderitem.vo;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Kakao {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="kakao_id")
    private Long ocid;
    
    @Column(name="partner_order_id")
    private String partner_order_id; // 가맹점 주문 번호
    
    @Column(name="partner_user_id")
    private String partner_user_id; // 가맹점 회원 id
    
    @Column(name="approved_at")
    private String approved_at; // 결제 승인 시간
    
    @Column(name="item_name")
    private String item_name; // 상품명
    
    @Embedded
    private Amount amount; // 결제 금액 정보
    
    private int quantity; // 상품 수량
    
    @Column(name="payment_method_type")
    private String payment_method_type; // 결제 수단
}
