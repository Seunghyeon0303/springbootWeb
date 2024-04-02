package com.kh.Final_Project.orderitem.service;

import java.util.List;

import com.kh.Final_Project.orderitem.entity.Pocket;


public interface OrderItemService {
	public String getPrice(List<Pocket> pocketList);
	int getCount(List<Pocket> pocketList);
	StringBuffer getProductName(List<Pocket> pocketList);
}