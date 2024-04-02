package com.kh.Final_Project.customerorder.vo;

import java.time.LocalDateTime;
import java.util.List;

import com.kh.Final_Project.orderitem.vo.OrderItemForm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerOrderForm {
	
    private Long orderId;
    private Long customerId; // 타입을 Long으로 변경하여 Customer 엔티티의 ID와 일치시킴
    private String deliveryStatus;
    private String paymentMethod;
    private LocalDateTime orderDate;
    private String deliveryCost;
    private int price;
    private List<OrderItemForm> orderItems; // 주문 항목 정보를 담을 리스트 추가
}
