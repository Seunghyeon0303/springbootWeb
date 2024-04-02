package com.kh.Final_Project.admin.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.Final_Project.admin.service.AdminServiceImp;
import com.kh.Final_Project.ask.entity.Ask;
import com.kh.Final_Project.coupon.entity.Coupon;
import com.kh.Final_Project.coupon.vo.CouponForm;
import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.service.ProductService;
import com.kh.Final_Project.quitReason.entity.QuitReason;
import com.kh.Final_Project.util.AdminDailySales;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminServiceImp adminServiceImp;

	@Autowired
	private ProductService productService;

	/* 관리자페이지 들어가기(매출조회) */
	@GetMapping("/")
	public String admin(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "7") int size) {


		if (adminServiceImp.isAdmin(session)) {

			LocalDate currentDate = LocalDate.now(); // 현재 날짜
			int year = currentDate.getYear(); // 현재 년도
			int month = currentDate.getMonthValue(); // 현재 월

			System.out.println("***currentDate:" + currentDate);
			System.out.println("***year:" + year);
			System.out.println("***month:" + month);

			int monthlySales = adminServiceImp.calcMonthly(year, month);
			int yearlySales = adminServiceImp.calcYearly(year);

			Pageable pageable = PageRequest.of(page, size);
			Page<AdminDailySales> dailySalesList = adminServiceImp.getDailySales(pageable);

			int startPage = Math.max(1, dailySalesList.getNumber() - 1); // 시작페이지
			int endPage = Math.min(startPage + 4, dailySalesList.getTotalPages());

			// 모델에 매출액 추가
			model.addAttribute("thisMonth", month);
			model.addAttribute("thisYear", year);
			model.addAttribute("monthlySales", monthlySales);
			model.addAttribute("yearlySales", yearlySales);
			model.addAttribute("dailySalesList", dailySalesList);

			model.addAttribute("page", dailySalesList);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

			return "/admin/adminSales";
		}
		return "redirect:/";
	}

	/* 관리자페이지 회원조회 들어가기 */
	@GetMapping("/adminCustomer")
	public String adminCustomer(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(value = "condition", required = false) String condition,
			@RequestParam(value = "keyword", required = false) String keyword,
			@PageableDefault(size = 10, sort = "ocid", direction = Sort.Direction.DESC) Pageable pageable) {


		if (adminServiceImp.isAdmin(session)) {

			// 가입된 회원의 목록 불러오기 + 페이지처리 + 검색
			Page<Customer> customerList;

			if (condition != null && keyword != null && !keyword.isEmpty()) {
				// 검색 조건이 있을 경우, 검색 결과 조회
				customerList = adminServiceImp.searchCustomers(condition, keyword, pageable);
			} else {
				// 검색 조건이 없을 경우, 전체 회원 조회
				customerList = adminServiceImp.customerAll(page);
			}

			long totalCustomers = adminServiceImp.countAllCustomers();

			int startPage = Math.max(1, customerList.getNumber() - 1); // 시작페이지
			int endPage = Math.min(startPage + 4, customerList.getTotalPages()); // 최대 5페이지까지만 보임


			model.addAttribute("customer", customerList);
			model.addAttribute("page", customerList);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("condition", condition);
			model.addAttribute("keyword", keyword);
			model.addAttribute("totalCustomers", totalCustomers);
			return "admin/adminCustomer";

		}

		return "redirect:/";

	}

	/* 관리자페이지 회원조회 상세페이지 들어가기 */
	@GetMapping("/customerDetail")
	public String CustomerDetail(HttpSession session, Model model,
			@RequestParam(value = "cusOcid", required = false) Long ocid) {

		if (adminServiceImp.isAdmin(session)) {

			Optional<Customer> customer1 = adminServiceImp.customer(ocid);
			Customer customer2 = customer1.orElse(null);

			model.addAttribute("cus", customer2);

			return "admin/adminCustomerDetails";
		}

		return "redirect:/";

	}

	/* 관리자페이지 회원삭제 */
	@PostMapping("/customerDelete/{ocid}")
	public String customerDelete(HttpSession session, @PathVariable(value = "ocid", required = false) Long ocid) {
		if (adminServiceImp.isAdmin(session)) {

			adminServiceImp.customerDel(ocid);

			return "redirect:/admin/adminCustomer";

		}
		return "redirect:/";
	}

	/* 관리자페이지 1:1문의 들어가기 */
	@GetMapping("/adminAsk")
	public String adminAsk(HttpSession session, Model model,
			@RequestParam(defaultValue = "0") int page,
			@PageableDefault(size = 10, sort = "ocid", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "keyword", required = false) String keyword) {
		
		if (adminServiceImp.isAdmin(session)) {
			
			// 문의 목록 조회
			Page<Ask> adminAskList;
			
			if (keyword != null && !keyword.trim().isEmpty()) { // 검색내용 있을 때
				
		        adminAskList = adminServiceImp.searchAskList(keyword, pageable);
		        
		    } else { // 검색내용 없을 때 전체목록
		    	
		        adminAskList = adminServiceImp.adminAskAll(page);
		    }
			
			int startPage = Math.max(1, adminAskList.getNumber() - 1); // 시작페이지
			int endPage = Math.min(startPage + 4, adminAskList.getTotalPages()); // 최대 5페이지까지만 보임


			model.addAttribute("adminAskList", adminAskList);
			model.addAttribute("page", adminAskList);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("keyword", keyword);
			
			return "admin/adminAsk";

		}
		return "redirect:/";
	}
	/* 관리자페이지 게시관리 들어가기 */
	@GetMapping("/adminBoard")
	public String adminBoard(HttpSession session, Model model) {
		if (adminServiceImp.isAdmin(session)) {


			/* 공지사항 총 게시글 */
			long totalNoti = adminServiceImp.countAllNoti();
			/* FAQ 총 게시글 */
			long totalFaq = adminServiceImp.countAllFaq();
			/* 이벤트 총 게시글 */
			long totalEvent = adminServiceImp.countAllEvent();

			model.addAttribute("totalNoti", totalNoti);
			model.addAttribute("totalFaq", totalFaq);
			model.addAttribute("totalEvent", totalEvent);

			return "admin/adminBoardSetting";

		}
		return "redirect:/";
	}

	/* 관리자페이지 상품관리 목록 들어가기 */
	@GetMapping("/adminItem")
	public String adminItem(Model model, @RequestParam(defaultValue = "0") int page,
			@RequestParam(required = false) String search) {

		int pageSize = 6;

		Pageable pageable = PageRequest.of(page, pageSize);

		Page<Product> productList = null;

		if (search == null || search.equals("")) {
			productList = productService.getAllProduct(pageable);
		} else {
			productList = productService.searchByName(search, pageable);
		}

		model.addAttribute("search", search);
		model.addAttribute("productList", productList);

		return "admin/adminItem";
	}

	/* 관리자페이지 상품관리 등록 들어가기 */
	@GetMapping("/adminItemRegister")
	public String adminItemRegister(HttpSession session) {

		if (adminServiceImp.isAdmin(session)) {
			// 여기에 내용 적기
			return "admin/adminItemRegi";
		}
		return "redirect:/";
	}

	/* 관리자페이지 상품관리 수정 들어가기 */
	@GetMapping("/adminItemModify")
	public String adminItemModify(HttpSession session, String productId, Model model) {

		if (adminServiceImp.isAdmin(session)) {

			// 여기에 내용 적기

			Long id = Long.parseLong(productId);

			Product product = productService.findById(id);

			if (product == null) {
				return "redirect:/admin/adminItem";
			}
			String category = product.getCategory().toString();

			model.addAttribute("product", product);
			model.addAttribute("category", category);
			return "admin/adminItemModi";

		}
		return "redirect:/";

	}

	@PostMapping("/adminItemModify")
	public String adminItemModifyEdit(Product product, String category) {

		productService.adminItemModify(product);

		return "redirect:/admin/adminItem";
	}

	/* 관리자 쿠폰조회 페이지 들어가기 */
	@GetMapping("/adminCoupon")
	public String adminCoupon(HttpSession session, Model model, @RequestParam(defaultValue = "0") int page,
			@PageableDefault(size = 10, sort = "ocid", direction = Sort.Direction.DESC) Pageable pageable,
			@RequestParam(value = "keyword", required = false) String keyword) {

		if (adminServiceImp.isAdmin(session)) {

			// 가입된 회원의 목록 불러오기 + 페이지처리(검색포함)
			Page<Coupon> couponList;

			if (keyword != null && !keyword.trim().isEmpty()) { // 검색어 있을 때

				couponList = adminServiceImp.searchCouponList(keyword, pageable);

			} else { // 검색어 없을 때, 전체페이지

				couponList = adminServiceImp.adminCouponAll(page);
			}

			int startPage = Math.max(1, couponList.getNumber() - 1); // 시작페이지
			int endPage = Math.min(startPage + 4, couponList.getTotalPages()); // 최대 5페이지까지만 보임

			model.addAttribute("coupons", couponList);
			model.addAttribute("page", couponList);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);
			model.addAttribute("keyword", keyword);

			return "admin/adminCoupon";

		}
		return "redirect:/";
	}

	/* 관리자 페이지에서 쿠폰삭제 */
	@PostMapping("/couponDelete/{ocid}")
	public String couponDelete(HttpSession session, @PathVariable(value = "ocid", required = false) Long ocid) {

		if (adminServiceImp.isAdmin(session)) {

			adminServiceImp.couponDel(ocid);

			return "redirect:/admin/adminCoupon";

		}
		return "redirect:/";
	}

	/* 관리자 쿠폰등록 페이지 들어가기 */
	@GetMapping("/adminCouponRegister")
	public String adminCouponRegi(HttpSession session) {

		if (adminServiceImp.isAdmin(session)) {

			return "admin/adminCouponRegi";

		}
		return "redirect:/";
	}

	/* 관리자 쿠폰등록 페이지 등록 */
	@PostMapping("/adminCouponRegister")
	public String adminCouponRegister(HttpSession session, Model model, CouponForm couponForm,
			String deadlineDateString) {
		if (!adminServiceImp.isAdmin(session)) {
			return "redirect:/"; // 관리자가 아니면 메인 페이지로 리다이렉트
		}

		boolean result = adminServiceImp.couponInsert(couponForm, deadlineDateString);

		if (result) {
			return "/admin/couponResiSuccess";

		} else {
			return "/admin/couponResiFail";
		}
	}

	/* 관리자페이지 탈퇴사유 리스트 */
	@GetMapping("/adminQuitReason")
	public String adminQuitReason(HttpSession session, Model model) {
	    if (adminServiceImp.isAdmin(session)) {
	        log.info("adminQuitReason()");

	        long countType1 = adminServiceImp.countQuitReasonsByType("1"); // reason_type이 '1'인 데이터의 개수
	        long countType2 = adminServiceImp.countQuitReasonsByType("2");
	        long countType3 = adminServiceImp.countQuitReasonsByType("3");
	        long countType4 = adminServiceImp.countQuitReasonsByType("4");
	        long countType5 = adminServiceImp.countQuitReasonsByType("5");
	        long countType6 = adminServiceImp.countQuitReasonsByType("6");
	        long countType7 = adminServiceImp.countQuitReasonsByType("7");
	        long countType8 = adminServiceImp.countQuitReasonsByType("8");
	       
	        model.addAttribute("countType1", countType1); // 모델에 개수 추가
	        model.addAttribute("countType2", countType2);
	        model.addAttribute("countType3", countType3);
	        model.addAttribute("countType4", countType4);
	        model.addAttribute("countType5", countType5);
	        model.addAttribute("countType6", countType6);
	        model.addAttribute("countType7", countType7);
	        model.addAttribute("countType8", countType8);
	        
	        return "admin/adminQuitReason";
	    }
	    return "redirect:/";
	}
	/* 탈퇴사유 기타 리스트 */
	@GetMapping("/adminQuitReasonDetail")
	public String adminQuitReasonDetail(HttpSession session, Model model,
			@PageableDefault(size = 10) Pageable pageable) {
		
	    if (adminServiceImp.isAdmin(session)) {
	        Page<QuitReason> quitReasonDetails = adminServiceImp.getQuitReasonsByType("8", pageable);
	        //List<QuitReason> quitReasonDetails = adminServiceImp.getQuitReasonsByType("8");
	       
	        
	        int startPage = Math.max(1, quitReasonDetails.getNumber() - 1); // 시작페이지
			int endPage = Math.min(startPage + 4, quitReasonDetails.getTotalPages()); // 최대 5페이지까지만 보임
			model.addAttribute("quitReasonDetails", quitReasonDetails);
		    model.addAttribute("page", quitReasonDetails);
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);

	        return "admin/adminQuitReasonDetail";
	    }
	    return "redirect:/";
	}
	

}
