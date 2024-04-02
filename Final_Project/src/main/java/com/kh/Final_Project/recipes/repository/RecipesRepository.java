package com.kh.Final_Project.recipes.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kh.Final_Project.recipes.vo.Recipe;

public interface RecipesRepository extends JpaRepository<Recipe, Long>{
	
	List<Recipe> findByRcpPartsDtlsContaining(String productName);
	List<Recipe> findTop10ByRcpPartsDtlsContaining(String productName);
	
}
