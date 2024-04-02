package com.kh.Final_Project.util;

import java.time.LocalDate;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdminDailySales {
	
	 private LocalDate date;
	 private int dailySales;

	 public AdminDailySales(LocalDate date, int dailySales) {
		super();
		this.date = date;
		this.dailySales = dailySales;
	}
	 
}
