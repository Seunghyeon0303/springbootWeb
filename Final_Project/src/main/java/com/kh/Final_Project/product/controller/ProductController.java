package com.kh.Final_Project.product.controller;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.product.service.ProductService;
import com.kh.Final_Project.recipes.service.RecipesService;
import com.kh.Final_Project.recipes.vo.Recipe;
import com.kh.Final_Project.wishlist.service.WishListServiceImp;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService service;
	
	@Autowired
	private RecipesService recipeService;

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private WishListServiceImp wishListService;

	
	@ModelAttribute("wishListProductIds")
	public Set<Long> getWishListProductIds(HttpSession session) {
	    // username을 이용하여 사용자의 찜 목록에 있는 상품 ID 목록을 조회
	    return wishListService.getWishList(session);
	}
	
	@GetMapping("/mainProduct")
	public String mainProduct(Model model) {
		List<Product> vegetables = service.findByCategory(Product.Category.Vegetable);
		List<Product> meats = service.findByCategory(Product.Category.Meat);
		List<Product> seafood = service.findByCategory(Product.Category.Seafood);
		List<Product> fruit = service.findByCategory(Product.Category.Fruit);
		List<Product> mealkit = service.findByCategory(Product.Category.MealKit);
		model.addAttribute("vegetablesList", vegetables);
		model.addAttribute("meatsList", meats);
		model.addAttribute("seafoodList", seafood);
		model.addAttribute("fruitList", fruit);
		model.addAttribute("mealkitList", mealkit);
		
		return "product/mainProduct";
	}
	

	@GetMapping("/category")
	public String productList(Model model) {
		log.info("ProductController productList()");

		model.addAttribute("productListALl", null);

		return "product/productCategory";
	}

	@GetMapping("/detail")
	public String detail(Long id) {
		return "product/productDetail";
	}

	@GetMapping("/search")
	public String search(String productName, Model model, @RequestParam(defaultValue = "0") int page) {

		log.info("검색한 상품 이름 : {}", productName);
		
		log.info("page : {}",page);
		
		int pageSize = 6;
		Pageable pageable = PageRequest.of(page, pageSize);
		
		Page<Product> productList = service.searchByName(productName,pageable);
		
		List<List<Product>> productListChunks = service.chunkProductList(productList.toList());
		
		model.addAttribute("productListChunks", productListChunks);
		model.addAttribute("page", productList);
		
		model.addAttribute("productName", productName);

		return "product/productSearch";
	}

	@GetMapping("/fragment/{fragmentName}")
	public String getFragment(@PathVariable String fragmentName, Model model, HttpServletRequest request) {	
		
		List<Product> productList = service.categoryByName(fragmentName);
		
		List<List<Product>> productListChunks = service.chunkProductList(productList);
		
		model.addAttribute("productList", productList);
		model.addAttribute("productListChunks", productListChunks);
		model.addAttribute("productCategory", fragmentName);
		String fragment = "product/" + fragmentName;

		System.out.println(productList.get(0).getProductID());
		return fragment;
	}
	@GetMapping("/detail/{productId}")
    public String productDetail(@PathVariable("productId") Long productId, Model model) {
        log.info("Entering productDetail() for productId: {}", productId);

        
        Optional<Product> productOptional = service.findById2(productId);
        
        if (!productOptional.isPresent()) {
            log.warn("Product with ID {} not found.", productId);
            return "redirect:/error"; // 혹은 적절한 에러 페이지로 리다이렉션
        }

        Product product = productOptional.get();
        model.addAttribute("product", product);

        List<Recipe> recipes = recipeService.getRecipeById(productId);
        if (recipes != null && !recipes.isEmpty()) {
            log.info("Found {} recipes for product ID {}", recipes.size(), productId);
            model.addAttribute("recipes", recipes);
        } else {
            log.info("No recipes found for product ID {}", productId);
        }

        return "product/productDetail";
    }
}

