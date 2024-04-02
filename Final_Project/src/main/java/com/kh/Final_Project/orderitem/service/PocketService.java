package com.kh.Final_Project.orderitem.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.orderitem.entity.Pocket;
import com.kh.Final_Project.orderitem.repository.PocketRepository;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.repository.ProductRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PocketService {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private PocketRepository pocketRepository;

	// 고객에게 장바구니 생성
	public void createPocket(Pocket pocket) {
		pocketRepository.save(pocket);
	}

	// 장바구니 조회하기
	public List<Pocket> pocketList(Long ocid) {
		log.info("PocketService pocketList() 실행");

		// 사용자 ID에 해당하는 모든 장바구니 조회
		List<Pocket> pocketList = pocketRepository.findByCustomerOcid(ocid);

		log.info("pocketList : {}", pocketList);

		// 조회된 장바구니 리스트 반환
		return pocketList;
	}

	// 장바구니 상품 삭제하기
	@Transactional
	public void pocketDeleteByProductID(Long productID, Long ocid) {

		pocketRepository.deleteByProductProductIDAndCustomerOcid(productID, ocid);
	}

	// 장바구니 총 개수 가져오기
	public int pocketCount(Long ocid) {
		return pocketRepository.countByCustomerOcid(ocid);
	}

	@Transactional
	// 장바구니 전체 삭제하기
	public void pocketDeleteAll(Long ocid) {
		pocketRepository.deleteAllByCustomerOcid(ocid);

	}

	public boolean addPocket(Map<String, Object> data, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		String productIDString = (String) data.get("productID");
		String countString = (String) data.get("count");

		int count = Integer.parseInt(countString);
		Long productID = Long.parseLong(productIDString);
		
		
		Optional<Product> productOpt = productRepository.findById(productID);
		if (productOpt.isPresent()) {

			Product product = productOpt.get();
			
			Pocket pocket = pocketRepository.findByCustomerAndProduct(loggedInCustomer, product);
			
			if(pocket == null) {
				pocket = new Pocket().builder().customer(loggedInCustomer).count(count).product(product).build();
			}else {
				int tempCount = pocket.getCount()+count;
				if(tempCount>=10) {
					tempCount = 10;
				}
				pocket.setCount(tempCount);
			}
			
			pocketRepository.save(pocket);
			
			return true;
		}
		return false;
	}

	public boolean addToCart(Map<String, Object> data, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		String productIDString = (String) data.get("productID");

		int count = 1;
		Long productID = Long.parseLong(productIDString);

		Optional<Product> productOpt = productRepository.findById(productID);
		if (productOpt.isPresent()) {

			Product product = productOpt.get();
			Pocket pocket = pocketRepository.findByCustomerAndProduct(loggedInCustomer, product);
			
			if(pocket == null) {
				log.info("없엉");
				pocket = new Pocket().builder().customer(loggedInCustomer).count(count).product(product).build();
			}else {
				int tempCount = pocket.getCount()+count;
				if(tempCount>=10) {
					tempCount = 10;
				}
				pocket.setCount(tempCount);
			}
			pocketRepository.save(pocket);
			
			return true;
		}
		return false;
	}

	// 주문 된 장바구니만 보여주기 즉 주문서 페이지
	public List<Pocket> OrderPocket(List<Long> productId, List<String> count, List<Boolean> check,
			List<String> priceAll, HttpServletRequest request) {

		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

		// 사용자 ID에 해당하는 모든 장바구니 조회
		List<Pocket> orderedPockets = new ArrayList<>();

		// 주문한 상품의 아이디와 갯수를 확인하고, check가 true인 경우만 포함
		for (int i = 0; i < productId.size(); i++) {
			if (check.get(i)) {
				Long id = productId.get(i); // 상품 ID 가져오기

				// Product 객체 생성
				Optional<Product> productOpt = productRepository.findById(id);
				if (productOpt.isPresent()) {
					Product product = productOpt.get();
					int itemCount = Integer.parseInt(count.get(i)); // 상품 개수 가져오기

					// Pocket 객체 생성 및 설정
					Pocket pocket = new Pocket();
					pocket.setCount(itemCount);
					pocket.setProduct(product);
					pocket.setCustomer(loggedInCustomer);
					orderedPockets.add(pocket);
				}
			}
			
		}	
		// 체크 된 장바구니 품목
		session.setAttribute("orderedPockets", orderedPockets);
		return orderedPockets;

	}

	// 포켓에 있는 개수 변경하기
	public void countChange(int changeCount, Long pocketId) {
		Pocket pocket = pocketRepository.findById(pocketId).orElse(null);
		if (pocket != null) {
			pocket.setCount(changeCount);
		}
		pocketRepository.save(pocket);
	}

}
