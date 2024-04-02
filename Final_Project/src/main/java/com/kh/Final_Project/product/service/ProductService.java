package com.kh.Final_Project.product.service;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.customerorder.repository.CustomerOrderRepostiory;
import com.kh.Final_Project.orderitem.repository.OrderItemRepository;
import com.kh.Final_Project.orderitem.repository.PocketRepository;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.entity.Product.Category;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.wishlist.repository.WishListRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private WishListRepository wishListRepository;
	
	@Autowired
	private PocketRepository pocketRepository;
	
	@Autowired
	private OrderItemRepository orderItemRepository;
	
	
	public int countProduct() {
		return (int) productRepository.count();
	}

	public void resetProductData() {

		if (countProduct() == 0) { // 데이터베이스가 비어있는 경우에만 삽입
			log.info("상품 데이터 비어있음");
			List<Product> products = new ArrayList<>();
			// 고기 추가
			products.add(new Product("소고기 등심", "300g", "19,000", "/img/product/meat/beef2.jpg", Category.Meat));
			products.add(new Product("소고기 채끝", "300g", "19,000", "/img/product/meat/beef3.jpg", Category.Meat));
			products.add(new Product("소고기 우둔", "1Kg", "24,900", "/img/product/meat/beef4.jpg", Category.Meat));
			products.add(new Product("소고기 양지", "200g", "14,900", "/img/product/meat/beef5.jpg", Category.Meat));
			products.add(new Product("소고기 목심", "300g", "14,500", "/img/product/meat/beef6.jpg", Category.Meat));
			products.add(new Product("소고기 갈비", "300g", "26,900", "/img/product/meat/beef7.jpg", Category.Meat));
			products.add(new Product("소고기 설도", "100g", "3,600", "/img/product/meat/beef8.jpg", Category.Meat));
			products.add(new Product("돼지고기 갈비", "1Kg", "12,000", "/img/product/meat/pork1.jpg", Category.Meat));
			products.add(new Product("돼지고기 목살", "500g", "10,000", "/img/product/meat/pork2.jpg", Category.Meat));
			products.add(new Product("돼지고기 항정살", "500g", "12,900", "/img/product/meat/pork3.jpg", Category.Meat));
			products.add(new Product("돼지고기 등심", "500g", "7,200", "/img/product/meat/pork4.jpg", Category.Meat));
			products.add(new Product("돼지고기 안심", "500g", "10,000", "/img/product/meat/pork5.jpg", Category.Meat));
			products.add(new Product("돼지고기 삼겹살", "500g", "9,900", "/img/product/meat/pork6.jpg", Category.Meat));
			products.add(new Product("돼지고기 사태", "1Kg", "18,800", "/img/product/meat/pork7.jpg", Category.Meat));
			products.add(new Product("돼지고기 갈매기살", "300g", "10,900", "/img/product/meat/pork8.jpg", Category.Meat));
			products.add(new Product("양고기 숄더랙", "1Kg", "41,900", "/img/product/meat/lamb1.jpg", Category.Meat));
			products.add(new Product("양고기 프랜치랙", "300g", "29,900", "/img/product/meat/lamb2.jpg", Category.Meat));
			products.add(new Product("양고기 큐브", "1Kg", "37,800", "/img/product/meat/lamb3.jpg", Category.Meat));
			products.add(new Product("닭고기", "1Kg", "8,000", "/img/product/meat/chicken1.jpg", Category.Meat));
			products.add(new Product("닭고기 가슴살", "1Kg", "6,500", "/img/product/meat/chicken2.jpg", Category.Meat));
			products.add(new Product("닭고기 다리", "1Kg", "14,000", "/img/product/meat/chicken3.jpg", Category.Meat));
			products.add(new Product("닭고기 안심", "1Kg", "7,500", "/img/product/meat/chicken4.jpg", Category.Meat));
			products.add(new Product("닭고기 날개", "2Kg", "15,900", "/img/product/meat/chicken5.jpg", Category.Meat));
			products.add(new Product("오리고기", "2Kg", "20,000", "/img/product/meat/duck1.jpg", Category.Meat));
			products.add(new Product("오리고기 목살", "1Kg", "15,300", "/img/product/meat/duck2.jpg", Category.Meat));
			products.add(new Product("오리고기 가슴살", "1Kg", "14,400", "/img/product/meat/duck3.jpg", Category.Meat));
			products.add(new Product("오리고기 안심", "2Kg", "24,500", "/img/product/meat/duck4.jpg", Category.Meat));
			products.add(new Product("오리고기 날개", "2Kg", "10,200", "/img/product/meat/duck5.jpg", Category.Meat));

			// 채소 추가
			products.add(new Product("깻잎", "30g", "1,300", "/img/product/vegetable/vegetable01_c.jpg", Category.Vegetable));
			products.add(new Product("상추", "30g", "1,300", "/img/product/vegetable/vegetable02_s.jpg", Category.Vegetable));
			products.add(new Product("양배추", "500g", "3,000", "/img/product/vegetable/vegetable03_y.jpg", Category.Vegetable));
			products.add(new Product("알배기배추", "800g", "4,000", "/img/product/vegetable/vegetable04_b.jpg", Category.Vegetable));
			products.add(new Product("무", "600g", "1,500", "/img/product/vegetable/vegetable05_m.jpg", Category.Vegetable));
			products.add(new Product("대파", "300g", "2,500", "/img/product/vegetable/vegetable06_p.jpg", Category.Vegetable));
			products.add(new Product("절단대파", "500g", "4,000", "/img/product/vegetable/vegetable07_p2.jpg", Category.Vegetable));
			products.add(new Product("깐마늘", "80g", "900", "/img/product/vegetable/vegetable08_m.jpg", Category.Vegetable));
			products.add(new Product("다진마늘", "250g", "4,500", "/img/product/vegetable/vegetable09_m2.jpg", Category.Vegetable));
			products.add(new Product("감자", "500g", "2,500", "/img/product/vegetable/vegetable10_po.jpg", Category.Vegetable));
			products.add(new Product("고구마", "500g", "3,000", "/img/product/vegetable/vegetable11_swpo.jpg", Category.Vegetable));
			products.add(new Product("오이", "400g", "1,500", "/img/product/vegetable/vegetable12_cu.jpg", Category.Vegetable));
			products.add(new Product("당근", "500g", "2,500", "/img/product/vegetable/vegetable13_ca.jpg", Category.Vegetable));
			products.add(new Product("새송이버섯", "600g", "2,800", "/img/product/vegetable/vegetable14_mu.jpg", Category.Vegetable));
			products.add(new Product("파프리카", "200g", "4,000", "/img/product/vegetable/vegetable15_pa.jpg", Category.Vegetable));
			products.add(new Product("풋고추", "150g", "3,500", "/img/product/vegetable/vegetable16_spa.jpg", Category.Vegetable));
			products.add(new Product("콩나물", "500g", "1,200", "/img/product/vegetable/vegetable17_k.jpg", Category.Vegetable));
			products.add(new Product("양파", "800g", "2,800", "/img/product/vegetable/vegetable18_oni.jpg", Category.Vegetable));
			products.add(new Product("시금치", "300g", "3,500", "/img/product/vegetable/vegetable19_s.jpg", Category.Vegetable));
			products.add(new Product("부추", "500g", "4,000", "/img/product/vegetable/vegetable20_b.jpg", Category.Vegetable));

			// 과일 추가
			products.add(new Product("딸기", "500g", "22,500", "/img/product/fruit/strawberry.jpg", Category.Fruit));
			products.add(new Product("수박", "4.5Kg", "37,800", "/img/product/fruit/watermelon.jpg", Category.Fruit));
			products.add(new Product("참외", "1.2Kg", "18,900", "/img/product/fruit/koreanmelon.jpg", Category.Fruit));
			products.add(new Product("무화과", "1Kg", "16,000", "/img/product/fruit/fig.jpg", Category.Fruit));
			products.add(new Product("배", "1Kg", "12,100", "/img/product/fruit/pear.jpg", Category.Fruit));
			products.add(new Product("복숭아", "1Kg", "24,900", "/img/product/fruit/peach.jpg", Category.Fruit));
			products.add(new Product("블루베리", "100g", "10,200", "/img/product/fruit/blueberry.jpg", Category.Fruit));
			products.add(new Product("자두", "800g", "11,800", "/img/product/fruit/plum.jpg", Category.Fruit));
			products.add(new Product("골드키위", "600g", "14,500", "/img/product/fruit/goldkiwi.jpg", Category.Fruit));
			products.add(new Product("포도", "2Kg", "17,500", "/img/product/fruit/grape.jpg", Category.Fruit));
			products.add(new Product("감", "500g", "8,000", "/img/product/fruit/persimmon.jpg", Category.Fruit));
			products.add(new Product("사과", "1.5Kg", "16,900", "/img/product/fruit/apple.jpg", Category.Fruit));
			products.add(new Product("석류", "1Kg", "14,900", "/img/product/fruit/pomegranate.jpg", Category.Fruit));
			products.add(new Product("귤", "1.5Kg", "15,000", "/img/product/fruit/mandarin.jpg", Category.Fruit));
			products.add(new Product("한라봉", "1.8Kg", "19,800", "/img/product/fruit/hanrabong.jpg", Category.Fruit));
			products.add(new Product("샤인머스캣", "600g", "15,900", "/img/product/fruit/shinemuscat.jpg", Category.Fruit));
			products.add(new Product("바나나", "1.2Kg", "6,400", "/img/product/fruit/banana.jpg", Category.Fruit));
			products.add(new Product("방울토마토", "500g", "8,700", "/img/product/fruit/cherrytomato.jpg", Category.Fruit));
			products.add(new Product("천혜향", "1Kg", "16,900", "/img/product/fruit/cheonhyehyang.jpg", Category.Fruit));
			products.add(new Product("파인애플", "2Kg", "9,800", "/img/product/fruit/pineapple.jpg", Category.Fruit));

			// 밀키트 추가
			products.add(new Product("매운돼지갈비찜", "790g", "11,500", "/img/product/mealkit/SpicyPigGalbi.jpg", Category.MealKit));
			products.add(new Product("하남쭈꾸미", "500g", "31,500", "/img/product/mealkit/HanamJJuggumi.jpg", Category.MealKit));
			products.add(new Product("쓰촨마라탕", "600g", "16,800", "/img/product/mealkit/MalaSoup.jpg", Category.MealKit));
			products.add(new Product("홍익육개장", "750g", "29,700", "/img/product/mealkit/Yukgaejang.jpg", Category.MealKit));
			products.add(new Product("우거지감자탕", "3Kg", "18,900", "/img/product/mealkit/Gamjatang.jpg", Category.MealKit));
			products.add(new Product("순살아구찜", "380g", "9,900", "/img/product/mealkit/Monkfish.jpg", Category.MealKit));
			products.add(new Product("베트남쌀국수소고기맛", "650g", "17,490", "/img/product/mealkit/VietnameseRiceNoodles.jpg", Category.MealKit));
			products.add(new Product("블랙라벨스테이크", "528g", "17,900", "/img/product/mealkit/Steak.jpg", Category.MealKit));
			products.add(new Product("바질크림빠네파스타", "636g", "10,900", "/img/product/mealkit/Pane.jpg", Category.MealKit));
			products.add(new Product("멸치칼국수", "1Kg", "9,900", "/img/product/mealkit/Kalguksu.jpg", Category.MealKit));
			products.add(new Product("들깨소불고기곱창전골", "1.2Kg", "18,900", "/img/product/mealkit/Gopchangjeongol.jpg", Category.MealKit));
			products.add(new Product("돼지고기김치찌개", "460g", "3,850", "/img/product/mealkit/KimchiJjigae.jpg", Category.MealKit));
			products.add(new Product("선지해장국", "600g", "13,900", "/img/product/mealkit/SeonjiHangoverSoup.jpg", Category.MealKit));
			products.add(new Product("프리미엄부대찌개", "930g", "16,000", "/img/product/mealkit/Budaejjigae.jpg", Category.MealKit));
			products.add(new Product("갈비탕", "2.4Kg", "18,900", "/img/product/mealkit/Galbitang.jpg", Category.MealKit));
			products.add(new Product("전복수삼삼계탕", "1Kg", "26,200", "/img/product/mealkit/Samgyetang.jpg", Category.MealKit));
			products.add(new Product("물냉면", "1.9Kg", "13,900", "/img/product/mealkit/Naengmyeon.jpg", Category.MealKit));
			products.add(new Product("해초비빔냉면", "880g", "22,900", "/img/product/mealkit/SpicyNaengmyeon.jpg", Category.MealKit));
			products.add(new Product("중화짬뽕", "1Kg", "13,900", "/img/product/mealkit/Jjamppong.jpg", Category.MealKit));
			products.add(new Product("김치돈까스나베", "1.8Kg", "48,300", "/img/product/mealkit/Kimchiporkcutletnabe.jpg", Category.MealKit));
			products.add(new Product("밀떡볶이", "780g", "12,480", "/img/product/mealkit/Tteokbokkimadeofwheat.jpg", Category.MealKit));
			products.add(new Product("매콤양념돼지껍데기", "350g", "8,400", "/img/product/mealkit/SpicyPorkRinds.jpg", Category.MealKit));
			products.add(new Product("고등어조림", "640g", "16,600", "/img/product/mealkit/BraisedMackerel.jpg", Category.MealKit));
			products.add(new Product("황제수라소머리꼬리곰탕", "4Kg", "69,800", "/img/product/mealkit/Kkorigomtang.jpg", Category.MealKit));
			products.add(new Product("라구생파스타", "300g", "9,000", "/img/product/mealkit/RaguPasta.jpg", Category.MealKit));
			products.add(new Product("차돌된장찌개", "1.84Kg", "26,200", "/img/product/mealkit/BeefBrisketSoybeanPasteJjigae.jpg", Category.MealKit));
			products.add(new Product("로제떡볶이", "590g", "10,000", "/img/product/mealkit/RoseTteokbokki.jpg", Category.MealKit));
			products.add(new Product("매운샤브샤브칼국수", "1.3Kg", "39,300", "/img/product/mealkit/SpicyShabuShabu.jpg", Category.MealKit));

			// 해산물 추가
			products.add(new Product("가리비", "1kg", "6,000", "/img/product/seafood/가리비.jpg", Category.Seafood));
			products.add(new Product("갑오징어", "1kg", "16,000", "/img/product/seafood/갑오징어.jpg", Category.Seafood));
			products.add(new Product("굴", "1kg", "8,000", "/img/product/seafood/굴.jpg", Category.Seafood));
			products.add(new Product("대게", "1kg", "30,000", "/img/product/seafood/대게.jpg", Category.Seafood));
			products.add(new Product("문어", "1kg", "6,000", "/img/product/seafood/문어.jpg", Category.Seafood));
			products.add(new Product("랍스터", "600g", "40,000", "/img/product/seafood/랍스터.jpg", Category.Seafood));
			products.add(new Product("멍게", "1kg", "5,000", "/img/product/seafood/멍게.jpg", Category.Seafood));
			products.add(new Product("바지락", "1kg", "4,500", "/img/product/seafood/바지락.jpg", Category.Seafood));
			products.add(new Product("새우", "1kg", "19,000", "/img/product/seafood/새우.jpg", Category.Seafood));
			products.add(new Product("고등어", "1kg", "14,000", "/img/product/seafood/손질고등어.jpg", Category.Seafood));
			products.add(new Product("연어", "1kg", "40,000", "/img/product/seafood/손질연어.jpg", Category.Seafood));
			products.add(new Product("장어", "1kg", "36,000", "/img/product/seafood/손질장어.jpg", Category.Seafood));
			products.add(new Product("전복", "1kg", "30,000", "/img/product/seafood/손질전복.jpg", Category.Seafood));
			products.add(new Product("참치", "1kg", "70,000", "/img/product/seafood/손질참치.jpg", Category.Seafood));
			products.add(new Product("오징어", "500g", "8,000", "/img/product/seafood/오징어.jpg", Category.Seafood));
			products.add(new Product("제주은갈치", "1kg", "30,000", "/img/product/seafood/제주은갈치.jpg", Category.Seafood));
			products.add(new Product("쭈꾸미", "500g", "15,000", "/img/product/seafood/쭈꾸미.jpg", Category.Seafood));
			products.add(new Product("블랙타이거새우", "500g", "50,000", "/img/product/seafood/킹블랙타이거새우.jpg", Category.Seafood));
			products.add(new Product("해삼", "1kg", "18,000", "/img/product/seafood/해삼.jpg", Category.Seafood));
			products.add(new Product("홍합", "1kg", "12,000", "/img/product/seafood/홍합.jpg", Category.Seafood));


			productRepository.saveAll(products);
			log.info("상품 데이터 생성 완료");

		} else {
			log.info("상품 데이터 있음");
		}

	}

	/**
	 * @param pageable
	 * @return 상품 전체 리스트 반환
	 */
	public Page<Product> getAllProduct(Pageable pageable) {

		Page<Product> list = productRepository.findAll(pageable);

		return list;
	}

	/**
	 * @param productName 상품 이름
	 * @param pageable    페이징
	 * @return 상품 이름이 포함된 검색 결과값
	 */
	public Page<Product> searchByName(String productName, Pageable pageable) {

		Page<Product> list = productRepository.findByProductNameContaining(productName, pageable);
		
		return list;
	}

	public List<List<Product>> chunkProductList(List<Product> products) {
		List<List<Product>> chunkedList = new ArrayList<>();
		List<Product> chunk = new ArrayList<>(3);

		for (Product product : products) {
			chunk.add(product);
			if (chunk.size() == 3) {
				chunkedList.add(new ArrayList<>(chunk));
				chunk.clear();
			}
		}

		if (!chunk.isEmpty()) {
			chunkedList.add(chunk); // 남은 항목 추가
		}

		return chunkedList;
	}
	
	public List<Product> findByCategory(Category category) {
		return productRepository.findByCategory(category);
	}

	public List<Product> categoryByName(String fragmentName) {

		Product.Category category;

		switch (fragmentName) {
		case "productCategoryMealKit":
			category = Product.Category.MealKit;
			break;
		case "productCategoryMeat":
			category = Product.Category.Meat;
			break;
		case "productCategorySeafood":
			category = Product.Category.Seafood;
			break;
		case "productCategoryFruit":
			category = Product.Category.Fruit;
			break;
		case "productCategoryVegetable":
			category = Product.Category.Vegetable;
			break;
		default:
			category = null;
			break;
		}

		List<Product> list = null;

		if (category != null) {
			list = productRepository.findByCategory(category);
		} else {
			list = productRepository.findAll();
		}

		return list;
	}


	/**
	 * 
	 * @param product 상품 엔티티를 가져와 수정
	 */
	@Transactional
	public void adminItemModify(Product product) {

		Long id = product.getProductID();

		Product target = productRepository.findById(id).orElse(null);

		if (target == null) {
			return;
		}
		
		target.patch(product);

		productRepository.save(target);

	}

	/**
	 * @param id 삭제할 상품의 id
	 * @return 삭제 성공시 true
	 */
	@Transactional
	public boolean deleteById(Long id) {
		Product product = productRepository.findById(id).orElse(null);
		if (product == null)
			return false;
		
		wishListRepository.deleteByProduct(product);
		pocketRepository.deleteByProduct(product);
		orderItemRepository.deleteByProduct(product);
		productRepository.delete(product);

		return true;
	}
	
	public Product findById(Long id) {
		Optional<Product> productOpt = productRepository.findById(id);
		if (productOpt.isEmpty()) {
			return null;
		}
		return productOpt.get();

	}

	public Optional<Product> findById2(Long productId) {
		return productRepository.findById(productId);
	}

	/**
	 * @param productQuantities
	 * @return 재고가 부족한 상품 리스트 재고가 모두 있다면 size가 0 해당 상품이 없으면 null
	 */
	public List<Product> checkStock(List<Map<String, Object>> productQuantities) {
		
		List<Product> list = new ArrayList<Product>();
		
        for (Map<String, Object> productQuantity : productQuantities) {
            Long productId = Long.valueOf((Integer) productQuantity.get("productId")); // JSON에서 int로 넘어올 수 있으므로 캐스팅 필요
            int count = (int) productQuantity.get("count");
            
            Product product = productRepository.findById(productId).orElse(null);
            
            if(product == null) {
            	return null;
            }
            if(product.getCount() < count ) {
            	list.add(product);
            }
            
        }
        
        return list;
		
	}
	

	
	
	
	

}
