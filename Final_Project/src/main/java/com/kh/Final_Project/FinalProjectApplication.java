package com.kh.Final_Project;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


import org.springframework.beans.factory.annotation.Autowired;

import com.kh.Final_Project.customer.service.CustomerServiceImp;
import com.kh.Final_Project.event.service.EventServiceImp;
import com.kh.Final_Project.faq.service.FAQServiceImp;
import com.kh.Final_Project.notice.service.NoticeServiceImp;
import com.kh.Final_Project.product.service.ProductService;
import com.kh.Final_Project.recipes.service.RecipesService;

@SpringBootApplication
public class FinalProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(FinalProjectApplication.class, args);
    }
    
    @Autowired
    private RecipesService recipesService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CustomerServiceImp customerService;
    
    @Autowired
    private EventServiceImp eventService;
    
    @Autowired
    private FAQServiceImp faqService;
    
    @Autowired
    private NoticeServiceImp noticeService;
    
    @Bean
    public CommandLineRunner run() { // 프로젝트 시작시 데이터 삽입
    	
        return args -> {
        	recipesService.resetRecipesTable();
        	productService.resetProductData();
        	customerService.insertAdminId();
        	eventService.resetEventData();
        	faqService.resetFaqData();
        	noticeService.resetNoticeData();
        };
    }
}
