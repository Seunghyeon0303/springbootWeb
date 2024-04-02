package com.kh.Final_Project.wishlist.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.repository.CustomerRepository;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.wishlist.entity.WishList;
import com.kh.Final_Project.wishlist.repository.WishListRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WishListServiceImp implements WishListService {

	@Autowired
    private WishListRepository wishListRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CustomerRepository customerRepository;

	/**
	 * @return 0 : 해당상품 존재  X / 1 : 이미 찜목록에 존재 / 2 : 찜 성공
	 */
	@Override
	@Transactional
	public int addToWishList(String productIdStr, Customer customer) {
		log.info("데이터베이스에 추가 시도");
		log.info("고객 : {}",customer.toString());
		
		Long productId = Long.parseLong(productIdStr);
		
		Optional<Product> productOpt =	productRepository.findById(productId);
		
		if(productOpt.isEmpty())
			return 0;
		
		Product product = productOpt.get();
		
		log.info("상품 : {}",product.toString());
		log.info("고객 : {}",customer.toString());
		
		Optional<WishList> wishListOpt = wishListRepository.findByCustomerAndProduct(customer, product);
		
		
		if(wishListOpt.isPresent()) {
			wishListRepository.delete(wishListOpt.get());
			return 1;
		}
		
		WishList save = new WishList(customer, product);
		
		log.info("save : {}" , save.toString());
		
		wishListRepository.save(save);
		
		return 2;
	}
	
	@Override
	@Transactional
	public Page<WishList> getAllWishList(int page,HttpServletRequest request) {
		
		HttpSession session = request.getSession(false);
		
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		
		Long ocid = loggedInCustomer.getOcid();
		int pageSize = 200;
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<WishList> wishList = wishListRepository.findByCustomer_Ocid(ocid, pageable);
		
		log.info("찜 목록 데이터 : {}", wishList);
		
		return wishList;
	}
	
	@Transactional
	public Set<Long> getWishList(HttpSession session) {
		
		Customer customer = (Customer) session.getAttribute("loggedInCustomer");
		if(customer == null) {
			return new HashSet<>();
		}
		
		List<WishList> list = wishListRepository.findByCustomer(customer);
		
		if(list == null || list.size() == 0)
			return new HashSet<>();
		
		Set<Long> set = list.stream().map(wishList -> wishList.getProduct().getProductID()).collect(Collectors.toSet());
		
		return set;
	}
	
	/**
	 * 
	 * @param session
	 * @param productId 상품 고유 번호
	 * @return 0이면 실패 
	 */
	public boolean deleteWishList(HttpSession session,Long productId) {
		
		Customer customer = (Customer) session.getAttribute("loggedInCustomer");
		if(customer == null) {
			return false;
		}
		
		Optional<Product> productOpt = productRepository.findById(productId);
		
		if(productOpt.isEmpty())
			return false;
		
		Product product = productOpt.get();
		
		Optional<WishList> wishListOpt  = wishListRepository.findByCustomerAndProduct(customer, product);
		
		 if (wishListOpt.isPresent()) {
		        wishListRepository.delete(wishListOpt.get());
		        return true; // 성공적으로 삭제됨
		    } else {
		        return false; // 해당하는 위시리스트 항목을 찾을 수 없음
		    }
		
	}
}