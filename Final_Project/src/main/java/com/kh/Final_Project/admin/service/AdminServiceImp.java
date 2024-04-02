package com.kh.Final_Project.admin.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import java.sql.Date;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.ask.entity.Ask;
import com.kh.Final_Project.ask.repository.AskRepository;
import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.coupon.repository.CouponRepository;
import com.kh.Final_Project.coupon.vo.CouponForm;
import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.repository.CustomerRepository;
import com.kh.Final_Project.customerorder.repository.CustomerOrderRepostiory;
import com.kh.Final_Project.event.repository.EventRepository;
import com.kh.Final_Project.faq.repository.FAQRepository;
import com.kh.Final_Project.notice.repository.NoticeRepository;
import com.kh.Final_Project.quitReason.entity.QuitReason;
import com.kh.Final_Project.quitReason.repository.QuitReasonRepostiory;
import com.kh.Final_Project.util.AdminDailySales;
import com.kh.Final_Project.util.Util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminServiceImp implements AdminService {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private AskRepository askRepository;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private CustomerOrderRepostiory customerOrderRepository;
	
	@Autowired
	private NoticeRepository noticeRepository;
	
	@Autowired
	private FAQRepository faqRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Autowired
	private QuitReasonRepostiory quitReasonRepository;

	/**
	 * 
	 * @param session 세션에 저장된 고객 객체
	 * @return 로그인된 객체의 ID가 'ADMIN'이면 true
	 */
	public boolean isAdmin(HttpSession session) {

		Customer a = (Customer) session.getAttribute("loggedInCustomer");

		if (a == null || a.getCustomerId() == null || !a.getCustomerId().equalsIgnoreCase("admin")) {

			return false;
		}
		return true;

	}

	/** 관리자페이지 회원조회에서 가입된 회원을 모두 조회하는 메서드 (admin 제외) */
	public Page<Customer> customerAll(int page) {

		// 1페이지 당 몇개씩 보여줄건지
		int pageSize = 10;
		// Pageable에 넘기기
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("ocid").descending());

		Page<Customer> customerList = customerRepository.findByCustomerIdNot("admin", pageable);

		return customerList;
	}

	/* 관리자페이지 회원 단건 조회 */
	public Optional<Customer> customer(Long ocid) {

		Optional<Customer> customer = customerRepository.findById(ocid);

		return customer;
	}

	/* 관리자페이지 회원삭제 */
	public void customerDel(Long ocid) {

		customerRepository.deleteById(ocid);

	}

	/* 관리자페이지 회원조회 검색창 */
	public Page<Customer> searchCustomers(String searchOption, String keyword, Pageable pageable) {

		switch (searchOption) {
		case "customerName":
			return customerRepository.findByCustomerNameContaining(keyword, pageable);
		case "customerId":
			return customerRepository.findByCustomerIdContaining(keyword, pageable);
		case "customerNameId":
			return customerRepository.findByCustomerNameContainingOrCustomerIdContaining(keyword, keyword, pageable);
		default:
			return customerRepository.findAll(pageable);
		}

	}

	/* 관리자페이지 문의조회 */
	public Page<Ask> adminAskAll(int page) {

		// 1페이지 당 몇개씩 보여줄건지
		int pageSize = 10;
		// Pageable에 넘기기
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("ocid").descending());

		Page<Ask> adminAskList = askRepository.findAll(pageable);

		return adminAskList;

	}
	/* 관리자페이지 문의조회 검색창 */

	public Page<Ask> searchAskList(String keyword, Pageable pageable) {

		return askRepository.findByCustmerRealIdContaining(keyword, pageable);

	}

	/* 관리자페이지 쿠폰조회 페이지 */
	public Page<Coupon> couponAll(int page) {

		// 1페이지 당 몇개씩 보여줄건지
		int pageSize = 10;

		// Pageable에 넘기기
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("ocid").descending());
		Page<Coupon> couponList = couponRepository.findAll(pageable);

		return couponList;
	}

	/* 관리자페이지 쿠폰삭제 */
	@Transactional
	public void couponDel(Long ocid) {
		couponRepository.deleteById(ocid);
	}

	/* 관리자페이지 검색어 조회 */
	public Page<Coupon> searchCouponList(String keyword, Pageable pageable) {

		return couponRepository.findByCodeContaining(keyword, pageable);
	}

	/* 관리자페이지 쿠폰등록 */
	@Transactional
	public boolean couponInsert(CouponForm couponForm, String deadlineDateString) {

		List<Customer> allCustomers = customerRepository.findAll();
		couponForm.setDeadlineDate(Util.StrToLDT(deadlineDateString));

		for (Customer temp : allCustomers) {
			couponForm.setCustomer(temp);
			Coupon coupon = couponForm.toEntity();
			couponRepository.save(coupon);
		}
		return true;

	}

	@Override
	public Page<AdminDailySales> getDailySales(Pageable pageable) {

		Page<Object[]> results = customerOrderRepository.findDailySales(pageable);

		List<AdminDailySales> salesList = new ArrayList<>();

		for (Object[] result : results.getContent()) {

			LocalDate orderDate = ((Date) result[0]).toLocalDate();
			int dailySales = ((BigDecimal) result[1]).intValue();
			salesList.add(new AdminDailySales(orderDate, dailySales));
		}

		return new PageImpl<>(salesList, pageable, results.getTotalElements());

	}
	
	
	@Override
	public int calcMonthly(int year, int month) {
		// 월매출
		Integer monthlySales  = customerOrderRepository.findTotalSalesByMonth(year, month);
		
		if (monthlySales == null) {
			
		    monthlySales = 0; // 또는 적절한 기본값 설정
		    
		}
		
		return monthlySales;
	}
	
	@Override
    public int calcYearly(int year) {
        // 연매출
		Integer yearlySales  =customerOrderRepository.findTotalSalesByYear(year);
		if (yearlySales == null) {
			
			yearlySales = 0; // 또는 적절한 기본값 설정	    
		}
		return yearlySales;
    }
	/* 관리자페이지 쿠폰조회 페이지*/
	public Page<Coupon> adminCouponAll(int page) {
		
		// 1페이지 당 몇개씩 보여줄건지
		int pageSize = 10;
		
	    // Pageable에 넘기기
		Pageable pageable = PageRequest.of(page, pageSize, Sort.by("ocid").descending());
		Page<Coupon> couponList = couponRepository.findAll(pageable);
		
		return couponList;
	}
	
	
	/* 전체회원수 */
	@Override
	public long countAllCustomers() {
		return customerRepository.count()-1;
	}
	
	/* 공지사항 게시물 수 */
	@Override
	public long countAllNoti() {
		return noticeRepository.count();
	}
	/* FAQ 수 */
	@Override
	public long countAllFaq() {
		
		return faqRepository.count();
	}
	/* Event 수 */
	@Override
	public long countAllEvent() {
		// TODO Auto-generated method stub
		return eventRepository.count();
	}
	/* 탈퇴 사유 리스트(첫목록)*/
/*	public List<QuitReason> adminQuitAll() {
		
		//List<QuitReason> quitList
		
		return quitReasonRepository.findAll();
	}*/
	/* 탈퇴 사유 카운트 */
	public long countQuitReasonsByType(String reasonType) {
	    return quitReasonRepository.countByReasonType(reasonType);
	}
	/* 탈퇴 사유 기타일 경우, 사유리스트*/
	/*public List<QuitReason> getQuitReasonsByType(String reasonType) {
        return quitReasonRepository.findByReasonType(reasonType);
    }*/
	public Page<QuitReason> getQuitReasonsByType(String reasonType, Pageable pageable) {
	    return quitReasonRepository.findByReasonType(reasonType, pageable);
	}
}
