package com.kh.Final_Project.orderitem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemForm {
    private Long productOcid; // 상품 식별자
    private int price;        // 가격
    private int count;        // 수량
}