package com.kh.Final_Project.product.entity;

import java.util.Random;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productID;

    @Column(nullable = false, length = 255)
    private String productName;

    @Column(length = 255)
    private String weight;

    @Column(length = 255)
    private String price;
    
    @Column(length = 255)
    private String imageURL;
    
    @Column
    private int count;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    public enum Category {
        Vegetable, Meat, Seafood, Fruit, MealKit
    }
    
    public Product(String productName, String weight, String price, String imageURL, Category category) {
        this.productName = productName;
        this.weight = weight;
        this.price = price;
        this.imageURL = imageURL;
        this.category = category;
        Random r = new Random();
        this.count = r.nextInt(41)+30;
    }

    public Product() {}
    
    public Product(long parseLong, String string, String string2, String string3) {
		// TODO Auto-generated constructor stub
	}

	public void patch(Product product) {
        this.productName = product.productName;
        this.weight = product.weight;
        this.price = product.price;
        this.category = product.category;
        this.count = product.count;
    }
    
    
    
}
