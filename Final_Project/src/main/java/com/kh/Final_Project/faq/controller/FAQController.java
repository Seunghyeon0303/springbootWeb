package com.kh.Final_Project.faq.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.kh.Final_Project.admin.service.AdminServiceImp;
import com.kh.Final_Project.event.entity.Event;
import com.kh.Final_Project.faq.entity.FAQEntity;
import com.kh.Final_Project.faq.service.FAQServiceImp;
import com.kh.Final_Project.faq.vo.FAQForm;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/faq")
public class FAQController {

	@Autowired
	private FAQServiceImp faqService;

	@Autowired
	private AdminServiceImp adminService;

	// 고객이 볼 FAQ 목록 <완성>
	@GetMapping("/")
	public String faq(Model model, Long ocid, @RequestParam(defaultValue = "0") int page) {

		// 한 화면에 보여지는 기본 글의 수
		int pageSize = 10;
		// 페이징 처리
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<FAQEntity> list = null;
		// 게시글 가져오기
		list = faqService.findAll(pageable);

		model.addAttribute("faqs", list.getContent());
		model.addAttribute("page", list);

		model.addAttribute("faqsOrderDelivery", faqService.findByCategory("주문/배송"));
		model.addAttribute("faqsProduct", faqService.findByCategory("상품"));
		model.addAttribute("faqsMembership", faqService.findByCategory("회원가입/정보"));
		model.addAttribute("faqsCancellation", faqService.findByCategory("취소/교환/환불"));


		return "notice/faq";
	}

	// 관리자_FAQ 전체 목록 + 페이징 처리 <완성>
	@GetMapping("/faqList")
	public String faqList(Model model, HttpSession session, 
			@RequestParam(defaultValue = "0") int page) {

		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		if (!adminService.isAdmin(session)) {
			return "redirect:/";
		}

		// 한 화면에 보여지는 기본 글의 수
		int pageSize = 10;
		// 페이징 처리
		Pageable pageable = PageRequest.of(page, pageSize);
		Page<FAQEntity> list = null;
		// 게시글 가져오기
		list = faqService.findAll(pageable);
		model.addAttribute("faqs", list.getContent());
		model.addAttribute("page", list);
		return "admin/adminFaq";
	}

	// 관리자_FAQ 게시글 상세페이지 <완성>
	@GetMapping("/admin/adminFaqDetail/{ocid}")
	public String adminFaqDetail(Model model, HttpSession session, 
			@PathVariable("ocid") Long ocid) {

		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		if (!adminService.isAdmin(session)) {
			return "redirect:/";
		}

		// 데베에서 데이터 꺼내기
		FAQEntity faq = faqService.findFaqByOcid(ocid);

		// 모델에 값 전달
		model.addAttribute("faq", faq);

		return "admin/adminFaqDetail";
	}

	// 관리자_FAQ 게시글 수정페이지 (수정 전 내용을 보여주는 메서드) <완성>
	// 먼저 뷰 페이지로 이동
	@GetMapping("/admin/adminFaqEdit/{ocid}")
	public String adminFaqEditGet(Model model, HttpSession session, 
			@PathVariable("ocid") Long ocid) {

		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }
		
		// 수정할 글 데베에서 조회후 해당 1건의 글 보기
		FAQEntity updateFaq = faqService.findFaqByOcid(ocid);

		// 찾은 결과 모델에 담아서 뷰페이지로 이동
		model.addAttribute("faq", updateFaq);

		return "admin/adminFaqEdit"; // 수정 페이지로 이동
	}

	// 관리자_FAQ 게시글 수정페이지 (수정 후 내용을 위한 메서드) <완성>
	@PostMapping("/admin/adminFaqDetail/{ocid}")
	public String adminFaqEditPost(FAQForm faqVo, HttpSession session, 
			@PathVariable("ocid") Long ocid, Model model) {

		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }
		
		// 수정을 위해 데베 등록 글을 가져온다.
		FAQEntity updateFaq = faqService.edit(ocid, faqVo);

		if (updateFaq != null) {
			updateFaq.setTitle(faqVo.getTitle());
			updateFaq.setContent(faqVo.getContent());

			// 저장, 수정, 변경
			FAQEntity faq = faqService.saveFaq(updateFaq);
			model.addAttribute("faq", faq);
		}
		return "admin/adminFaqDetail";
	}

	// FAQ 삭제 <완성>
	@GetMapping("/admin/adminFaqDelete/{ocid}")
	public String adminFaqDeleteGet(HttpSession session, 
			@PathVariable Long ocid) {
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }
		
		faqService.deleteFaq(ocid);

		return "redirect:/faq/faqList";
	}

	// FAQ 게시글 작성 (작성 전, 저장 전) <완성>
	@GetMapping("/admin/adminFaqNew")
	public String adminFaqNew(HttpSession session) {

		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }
		
		return "admin/adminFaqNew";
	}

	// FAQ 게시글 작성 (작성 후, 저장) <완성>
	@PostMapping("admin/adminFaqCreate")
	public String adminFaqCreate(FAQForm faqVo, Model model, 
			HttpSession session) {
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }

		// Vo 데이터를 entity로 변경
		FAQEntity faq = faqVo.toEntity();

		// 저장
		FAQEntity saveFaq = faqService.createFaq(faq);

		model.addAttribute("faq", saveFaq);

		return "admin/adminFaqDetail";
	}
}