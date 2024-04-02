package com.kh.Final_Project.orderitem.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.repository.CustomerRepository;
import com.kh.Final_Project.customerorder.entity.CustomerOrder;
import com.kh.Final_Project.customerorder.repository.CustomerOrderRepostiory;
import com.kh.Final_Project.orderitem.entity.OrderItem;
import com.kh.Final_Project.orderitem.entity.Pocket;
import com.kh.Final_Project.orderitem.repository.OrderItemRepository;
import com.kh.Final_Project.orderitem.repository.PocketRepository;
import com.kh.Final_Project.orderitem.vo.Kakao;
import com.kh.Final_Project.orderitem.vo.KakaoApproveResponse;
import com.kh.Final_Project.orderitem.vo.KakaoReadyResponse;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.repository.ProductRepository;
import com.kh.Final_Project.util.Util;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableAsync
public class KakaoPayService {
	// 프로퍼티스에서 값을 꺼내와서 변수에 직접 저장
	@Value("${kakao.admin.key}")
	private String KAKAO_ADMIN_KEY;
	// 가맹점 테스트 코드
	static final String cid = "TC0ONETIME";

	@Autowired
	private PocketRepository pocketRepository;
	
	@Autowired
	private ProductRepository productRepository;

	private ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
	// 결제 승인 담는곳
	KakaoApproveResponse app;

	// 결제 준비 담는곳
	KakaoReadyResponse vo;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerOrderRepostiory customerOrderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	// KakaoPayService 클래스의 KakaoPayReady 메서드
	@Transactional
	public String KakaoPayReady(HttpServletRequest request, int count, int totalPrice, String productName,
			String recipient, String postcode, String roadAddress, String customerAddress3, String phonePrefix,
			String phoneSuffix, String phonePrefix1, String phoneSuffix1, String phonePrefixValue,
			String phonePrefixValue1) {

		HttpSession session = request.getSession(false);
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		OrderItem item = new OrderItem();
		CustomerOrder order = new CustomerOrder();

		item.setCustomerOrder(order);
		item.setCount(count);
		item.setPrice(totalPrice);

		order.setCustomer(loggedInCustomer);
		order.setPrice(totalPrice);
		order.setCount(count);
		order.setCustomerOrderName(
				(recipient != null && !recipient.isEmpty()) ? recipient : loggedInCustomer.getCustomerName());
		order.setCustomerOrderAddress1(
				(postcode != null && !postcode.isEmpty()) ? postcode : loggedInCustomer.getCustomerAddress1());
		order.setCustomerOrderAddress2(
				(roadAddress != null && !roadAddress.isEmpty()) ? roadAddress : loggedInCustomer.getCustomerAddress2());
		order.setCustomerOrderAddress3((customerAddress3 != null && !customerAddress3.isEmpty()) ? customerAddress3
				: loggedInCustomer.getCustomerAddress3());

		// 맨 앞 전화번호 설정
		if (phonePrefixValue != null && !phonePrefixValue.isEmpty() && phonePrefix != null && !phonePrefix.isEmpty()
				&& phoneSuffix != null && !phoneSuffix.isEmpty()) {
			String firstPhone = phonePrefixValue + '-' + phonePrefix + '-' + phoneSuffix;
			// 받은 번호가 값이 다 있다 그러면
			order.setCustomerOrderPhone(firstPhone);
		}
		// 두 번째 전화번호 설정
		if (phonePrefixValue1 != null && !phonePrefixValue1.isEmpty() && phonePrefix1 != null && !phonePrefix1.isEmpty()
				&& phoneSuffix1 != null && !phoneSuffix1.isEmpty()) {
			String lastPhone = phonePrefixValue1 + '-' + phonePrefix1 + '-' + phoneSuffix1;
			if (order.getCustomerOrderPhone() != null && !order.getCustomerOrderPhone().isEmpty()) {
				String phone = order.getCustomerOrderPhone();
				order.setCustomerOrderPhone(phone + ',' + lastPhone);
			} else {
				order.setCustomerOrderPhone(lastPhone);
			}
		}

		// 전송을 하기 위해서 필요한 resttemplate 얘도 똑같
		RestTemplate restTemplate = new RestTemplate();
		// 서버로 요청할 header
		HttpHeaders headers = new HttpHeaders();

		// 헤더 부분
		headers.add("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// 서버로 요청할 Body 얘도
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
		// body 부분
		params.add("cid", cid);
		params.add("partner_order_id", "GreenBox");
		params.add("partner_user_id", "FinalProject");
		// 이부분에 상품명 들어오는거로 밑에 3개는 변수로 하실?
		params.add("item_name", productName);
		params.add("quantity", count);
		params.add("total_amount", totalPrice);
		// 상품 비과세 금액
		params.add("tax_free_amount", 0);

		params.add("approval_url", "http://localhost:9080/payment/success");
		params.add("fail_url", "http://localhost:9080/payment/fail");
		params.add("cancel_url", "http://localhost:9080/payment/cancel");
		params.add("msg", "결제가 완료 되었습니다.");

		// 위에 두개의 내용을 하나로 묶어서 전송해야한다.
		HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<>(params, headers);
		// 잘들어갔늕지 확인

		// 전송
		vo = restTemplate.postForObject("https://kapi.kakao.com/v1/payment/ready", body, KakaoReadyResponse.class);

		// 잘 받아왔는지 보기

		// url 넘기고 데이터 도 같이 넘길거라서
//	    데이터 값 세션으로 넘기기
		session.setAttribute("order", order);
		session.setAttribute("loggedInCustomer", loggedInCustomer);
		session.setAttribute("item", item);

		return "redirect:" + vo.getNext_redirect_pc_url();
	}

	// 결제 완료한거 승인 받는거
	public Kakao KakaoApprove(String pg_token, HttpSession session) {
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		CustomerOrder order = (CustomerOrder) session.getAttribute("order");
		// 세션에서 주문된 Pocket 객체들을 꺼내기
		List<Pocket> orderedPockets = (List<Pocket>) session.getAttribute("orderedPockets");
		ArrayList<OrderItem> orderItemList = null;
		// orderedPockets가 null이 아닌 경우에만 실행
		if (orderedPockets != null) {
			orderItemList = new ArrayList<OrderItem>();
			for (Pocket pocket : orderedPockets) {
				// 각 루프에서 새로운 OrderItem 객체 생성
				OrderItem orderItem = new OrderItem();
				// OrderItem 객체의 필드 설정
				orderItem.setProduct(pocket.getProduct()); // Pocket에 있는 상품 정보를 가져와서 설정
				orderItem.setCount(pocket.getCount()); // Pocket에 있는 수량 정보를 가져와서 설정
				orderItem.setPrice(
						Integer.parseInt(pocket.getProduct().getPrice().replace(",", "")) * pocket.getCount());
				orderItemList.add(orderItem);
			}
		}


		RestTemplate restTemplate = new RestTemplate();
		// 위에랑 겹치니 여기는 내려서 쓰기
		HttpHeaders headers = new HttpHeaders();
		// 헤두 부분
		headers.add("Authorization", "KakaoAK " + KAKAO_ADMIN_KEY);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

		// 서버로 요청할 Body 얘도
		MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();

		// 바디 부분
		params.add("cid", cid);
		params.add("tid", vo.getTid());
		params.add("partner_order_id", "GreenBox");
		params.add("partner_user_id", "FinalProject");
		params.add("pg_token", pg_token);

		HttpEntity<MultiValueMap<String, Object>> body = new HttpEntity<>(params, headers);
		body = new HttpEntity<>(params, headers);

		Kakao kakao = restTemplate.postForObject("https://kapi.kakao.com/v1/payment/approve", body, Kakao.class);
		order.setOrderDate(LocalDateTime.parse(kakao.getApproved_at()));
		order.setPrice(kakao.getAmount().getTotal());
		order.setPaymentMethod("카카오결제");
		order.setDeliveryCost("3000원");
		order.setDeliveryStatus("배송 대기중");

		// 각각 db에 저장
		customerOrderRepository.save(order);
		
		updateDeliveryStatus(order);
		
		List<CustomerOrder> cusorderList = customerOrderRepository.findAllOrderByOrderIdDesc();
		CustomerOrder cusorder = cusorderList.get(0);

		for (OrderItem item : orderItemList) {
			item.setCustomerOrder(cusorder);
		}
		
		orderItemRepository.saveAll(orderItemList);
		customerRepository.save(loggedInCustomer);
		
		

		for (Pocket pocket : orderedPockets) {
			// Pocket 엔티티에서 고객과 제품을 가져옵니다.
			Customer customer = pocket.getCustomer();
			Product product2 = pocket.getProduct();
			// 고객과 제품 정보를 이용하여 Pocket 엔티티를 찾아서 삭제합니다.
			Pocket pocketToDelete = pocketRepository.findByCustomerAndProduct(customer, product2);
			
			product2.setCount(product2.getCount()-pocket.getCount());
			productRepository.save(product2);
			
			
			if (pocketToDelete != null) {
				pocketRepository.delete(pocketToDelete);
			}
		}

		return kakao;
	}
	
	@Async
	public void updateDeliveryStatus(CustomerOrder customerOrder) {
	    
		Runnable task = () -> {
			if(customerOrder.getDeliveryStatus().equals("배송 대기중")) {
				customerOrder.setDeliveryStatus("배송 중");
				customerOrderRepository.save(customerOrder);
			}
		};
		// Runnable 태스크를 2분 후에 실행하도록 스케줄링
		scheduler.schedule(task, 2, TimeUnit.MINUTES);
	}

	
    
}
