package com.kh.Final_Project.recipes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

import com.kh.Final_Project.recipes.service.RecipesService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/recipe")
public class RecipesController {

	@Autowired
	private RecipesService service;

	@GetMapping("/addRecipe")
	public String fetchDataFromApi() {
		
		log.info("레시피 컨트롤러");
		
		service.fetchRecipes();
		service.fetchRecipes2();
		
		// API 호출

		// 응답 데이터 처리
		// 여기에서는 간단히 응답을 그대로 반환하도록 작성했지만, 실제로는 데이터를 파싱하여 필요한 형태로 가공해야 합니다.
		return "index";
	}

}