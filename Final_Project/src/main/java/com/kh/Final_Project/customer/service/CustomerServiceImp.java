package com.kh.Final_Project.customer.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.repository.CustomerRepository;
import com.kh.Final_Project.customer.vo.CustomerForm;
import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.customerorder.repository.CustomerOrderRepostiory;
import com.kh.Final_Project.orderitem.repository.OrderItemRepository;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.util.Util;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CustomerServiceImp implements CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CustomerOrderRepostiory customerOrderRepostiory;

	@Override
	public Customer signUp(CustomerForm customerForm) {

		String hashedPw = Util.encodePassword(customerForm.getCustomerPw());
		customerForm.setCustomerPw(hashedPw);
		Customer entity = customerForm.toEntity();
		Customer saved = customerRepository.save(entity);
		return saved;

	}

	@Override
	public boolean idDuplicateCheck(String id) {
		return customerRepository.existsByCustomerId(id); // 주어진 ID가 존재하면 true, 그렇지 않으면 false를 반환합니다.
	}

	@Override
	public boolean login(String customerId, String customerPw, HttpServletRequest request) {

		Optional<Customer> userOpt = customerRepository.findByCustomerId(customerId);
		
		if (userOpt.isPresent()) {
			Customer customer = userOpt.get();
			if(customer.getCustomerPw().contains("oauthAPI로그인")) {
				return false;
			}
			
			// 데이터베이스에 저장된 해시화된 비밀번호와 사용자가 입력한 비밀번호의 해시값 비교
			if (Util.verifyPassword(customerPw, customer.getCustomerPw())) {
				// 로그인 성공
				HttpSession session = request.getSession();
				session.setAttribute("loggedInCustomer", customer);
				return true;
			}
		}
		return false;
	}

	/**
	 * @param customer      세션에 저장된 customer객체
	 * @param inputPassword 유저가 입력한 비밀번호
	 * 
	 * @return customer객체의 pw와 입력한 pw 같으면 true 다르면 false
	 */
	public boolean checkPw(Customer customer, String inputPassword) {

		String customerDbPw = customer.getCustomerPw();

		if (Util.verifyPassword(inputPassword, customerDbPw)) {
			return true;
		}
		return false;
	}

	@Override
	public Customer kakaoSignUp(CustomerForm customerForm, HttpServletRequest request) { // customerId,Email,Name만 있음

		String customerId = customerForm.getCustomerId();

		boolean check = idDuplicateCheck(customerId);

		if (!check) {
			System.out.println("kakaoSignUp에서 저장");
			Customer entity = customerForm.toEntity();
			Customer saved = customerRepository.save(entity);
			return saved;
		}

		Optional<Customer> userOpt = customerRepository.findByCustomerId(customerId);

		if (userOpt.isPresent()) {
			Customer customer = userOpt.get();
			// 데이터베이스에 저장된 해시화된 비밀번호와 사용자가 입력한 비밀번호의 해시값 비교
			// 로그인 성공
			HttpSession session = request.getSession();
			session.setAttribute("loggedInCustomer", customer);
		}

		return null;
	}

	@Override
	public Customer naverSignUp(CustomerForm customerForm, HttpServletRequest request) {

		String customerId = customerForm.getCustomerId();

		boolean check = idDuplicateCheck(customerId);

		if (!check) {
			Customer entity = customerForm.toEntity();
			Customer saved = customerRepository.save(entity);
			return saved;
		}

		Optional<Customer> userOpt = customerRepository.findByCustomerId(customerId);

		if (userOpt.isPresent()) {
			Customer customer = userOpt.get();
			// 데이터베이스에 저장된 해시화된 비밀번호와 사용자가 입력한 비밀번호의 해시값 비교
			// 로그인 성공
			HttpSession session = request.getSession();
			session.setAttribute("loggedInCustomer", customer);
		}

		System.out.println("NaverSignUp null 반환");
		return null;
	}

	public void logout(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if (session != null) {
			session.invalidate();
		}


	}
	
	@Transactional
	public boolean myInfoRe(Customer loggedInCustomer, CustomerForm customerForm) {

		if (customerForm.getCustomerPw() != null && !customerForm.getCustomerPw().equals("")) {
			loggedInCustomer.setCustomerPw(Util.encodePassword(customerForm.getCustomerPw()));
		}

		loggedInCustomer.setCustomerPhone(customerForm.getCustomerPhone());
		loggedInCustomer.setCustomerAddress1(customerForm.getCustomerAddress1());
		loggedInCustomer.setCustomerAddress2(customerForm.getCustomerAddress2());
		loggedInCustomer.setCustomerAddress3(customerForm.getCustomerAddress3());

		Customer updatedCustomer = customerRepository.save(loggedInCustomer);

		if (updatedCustomer != null) {
			return true;
		} else {
			return false;
		}

	}

	@Override
	public void insertAdminId() {

		Optional<Customer> adminOpt = customerRepository.findByCustomerId("admin");

		if (adminOpt.isEmpty()) { // ID가 admin 인 객체가 없다면 생성
			
			Customer admin = new Customer();

			admin.setCustomerId("admin");

			String adminPw = "admin1234";

			String hashedPw = Util.encodePassword(adminPw);

			admin.setCustomerPw(hashedPw);
			
			customerRepository.save(admin);
			
			log.info("어드민 생성 완료");
		}else {
			log.info("어드민 있음");
		}

	}
	
	
	public String findIdByEmail(String email) {
		
		Customer customer = customerRepository.findByCustomerEmail(email);
		
		if(customer == null) {
			return null;
		}
		
		return customer.getCustomerId();
	}

	public boolean findById(String id) {
		
		Optional<Customer> customerOpt = customerRepository.findByCustomerId(id);
		
		if(customerOpt.isPresent()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 
	 * @param map 아아디와 비밀번호
	 * @return 0: 해당 객체가 없음 , 1: oauth 로그인이라 비번 변경 불가 , 2: 현재 입력한 비밀번호와 저장된 비밀번호가 같음 , 3: 변경 성공
	 */
	@Transactional
	public int changePw(Map<String, String> map) {
		
	    String userId = map.get("userId");
	    String password = map.get("password");
		
	    Optional<Customer> customerOpt = customerRepository.findByCustomerId(userId);
	    
	    if(customerOpt.isEmpty())
	    	return 0;
	    
	    Customer customer = customerOpt.get();
	    
	    if(customer.getCustomerPw().contains("oauthAPI로그인")) {
	    	return 1;
	    }
	    
	    String hashedPw = Util.encodePassword(password);
	    
	    if(Util.verifyPassword(password, customer.getCustomerPw())) {
	    	return 2;
	    }
	    
	    customer.setCustomerPw(hashedPw);
	    
	    
	    customerRepository.save(customer);
		
		return 3;
	}
	
	@Override
    public Customer findCustomerById(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

	public List<CustomerOrder> findByCustomer(Customer customer) {
	    List<CustomerOrder> customerOrders = customerOrderRepostiory.findByCustomer(customer);
	    
	    return customerOrders;
	}
	
	/**
	 * @param customer
	 * @return 배달 상태가 취소/환불이 아닌 리스트
	 */
	public List<CustomerOrder> deliveryStatus(Customer customer) {
	    return customerOrderRepostiory.findByCustomerAndDeliveryStatusNot(customer, "취소/환불");
	}
	
	/**
	 * @param customer
	 * @return 배달 상태가 취소/환불이 아닌 리스트
	 */
	public List<CustomerOrder> deliveryStatusNot(Customer customer) {
	    return customerOrderRepostiory.findByCustomerAndDeliveryStatus(customer, "취소/환불");
	}
	

}
