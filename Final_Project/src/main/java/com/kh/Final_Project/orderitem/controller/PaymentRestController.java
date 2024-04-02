package com.kh.Final_Project.orderitem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/verify")
public class PaymentRestController {

    private final IamportClient iamportClient;

    @Autowired
    public PaymentRestController(IamportClient iamportClient) {
        this.iamportClient = iamportClient;
    }

    @PostMapping("/{imp_uid}")
    public IamportResponse<Payment> verifyPayment(@PathVariable("imp_uid") String imp_uid) {
        try {
            // 아임포트 클라이언트를 사용하여 결제를 확인하고 결과를 반환합니다.
            return iamportClient.paymentByImpUid(imp_uid);
        } catch (Exception e) {
            // 예외 처리
            // 예외에 따라 적절한 응답을 반환해야 합니다.
            return null; // 예시로 null 반환
        }
    }
}