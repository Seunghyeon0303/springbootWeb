package com.kh.Final_Project.notice.controller;

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
import com.kh.Final_Project.event.vo.EventForm;
import com.kh.Final_Project.notice.entity.NoticeEntity;
import com.kh.Final_Project.notice.service.NoticeServiceImp;
import com.kh.Final_Project.notice.vo.NoticeForm;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/notice")
public class NoticeController {

	@Autowired
	private NoticeServiceImp noticeService;
	
	@Autowired
	private AdminServiceImp adminService;
	
	// 고객이 볼 브랜드 소개 <완성>
	@GetMapping("/brand")
	public String brand() {
		return "notice/introBrand";
	}
	
	// 고객이 볼 이용약관_연결 <완성>
	@GetMapping("/policyService")
	public String policyService() {
		return "notice/policyService";
	}
	
	// 고객이 볼 개인정보처리지침_연결 <완성>
		@GetMapping("/policyPrivacy")
		public String policyPrivacy() {
			return "notice/policyPrivacy";
		}

	// 인재채용
	@GetMapping("/careers")
	public String careers() {
		return "notice/careers";
	}
	
	// 사이트맵
	@GetMapping("/siteMap")
	public String siteMap() {
		return "notice/siteMap";
	}

	// 고객이 볼 공지사항 목록 <완성>
	@GetMapping("/notice")
	public String notice(Model model, Long ocid,
			@RequestParam(defaultValue="0") int page) {
		
		// 한 화면에 보여지는 기본 글의 수
		int pageSize = 10;
		// 페이징 처리 
		Pageable pageable=PageRequest.of(page,pageSize);
		Page<NoticeEntity> list = null;
		
		// 게시글 가져오기
		list = noticeService.findAll(pageable);
		
		model.addAttribute("notices",list.getContent());
		model.addAttribute("page",list);
		
		return "notice/notice";
	}
	
	// 관리자_공지사항 전체 목록 + 페이징 처리 <완성>
		@GetMapping("/noticeList")
		public String noticeList(Model model, HttpSession session,
				@RequestParam(defaultValue="0") int page) {
			 
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			 
			// 한 화면에 보여지는 기본 글의 수
			int pageSize = 10;
			// 페이징 처리 
			Pageable pageable=PageRequest.of(page,pageSize);
			Page<NoticeEntity> list = null;
			// 게시글 가져오기
			list = noticeService.findAll(pageable);
			model.addAttribute("notices",list.getContent());
			model.addAttribute("page",list);
			return "admin/adminNotice";
		}	
		
		// 관리자_공지사항 게시글 상세페이지 <완성>
		@GetMapping("/admin/adminNoticeDetail/{ocid}")
		public String adminNoticeDetail(Model model, HttpSession session,
				@PathVariable("ocid") Long ocid) {
			
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			 			
			// 데베에서 데이터 꺼내기
			NoticeEntity notice = noticeService.findNoticeByOcid(ocid);		
			
			// 모델에 값 전달
			model.addAttribute("notice",notice);
			
			return "admin/adminNoticeDetail";
		}
		
		// 공지사항 게시글 작성 (작성 전, 저장 전) <완성>
		@GetMapping("/admin/adminNoticeNew")
		public String adminNoticeNew(HttpSession session) {
			
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			
			return "admin/adminNoticeNew";
		}
		
		// 공지사항 게시글 작성 (작성 후, 저장) <완성>
		@PostMapping("/admin/adminNoticeCreate")
		public String adminNoticeCreate(NoticeForm noticeVo,
				Model model, HttpSession session) {
			
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			
			// Vo 데이터를 entity로 변경
			NoticeEntity notice = noticeVo.toEntity();
			
			// 저장 
			NoticeEntity saveNotice = noticeService.createNotice(notice);
			
			model.addAttribute("notice",saveNotice);
			
			return "admin/adminNoticeDetail";
		}
		
		// 관리자_공지사항 게시글 수정페이지 (수정 전 내용을 보여주는 메서드) <완성>
		// 먼저 뷰 페이지로 이동
		@GetMapping("/admin/adminNoticeEdit/{ocid}")
		public String adminNoticeEditGet(Model model, HttpSession session,
				@PathVariable("ocid") Long ocid) {
			
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			
			// 수정할 글 데베에서 조회후 해당 1건의 글 보기
			NoticeEntity updateNotice = noticeService.findNoticeByOcid(ocid);
			
			// 찾은 결과 모델에 담아서 뷰페이지로 이동
			model.addAttribute("notice",updateNotice);
			
			return "admin/adminNoticeEdit";	//수정 페이지로 이동
		}
		
		// 관리자_이벤트 게시글 수정페이지 (수정 후 내용을 위한 메서드) <완성>
		@PostMapping("/admin/adminNoticeDetail/{ocid}")
		public String adminNoticeEditPost(
				 NoticeForm noticeVo, HttpSession session,
				@PathVariable("ocid") Long ocid,Model model) {
			
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			
			// 수정을 위해 데베 등록 글을 가져온다.
			NoticeEntity updateNotice = noticeService.edit(ocid, noticeVo);
					
			
			if(updateNotice != null) {
				updateNotice.setTitle(noticeVo.getTitle());
				updateNotice.setContent(updateNotice.getContent());

				// 저장, 수정, 변경
				NoticeEntity notice = noticeService.saveNotice(updateNotice);
				model.addAttribute("notice",notice);
			}		
			return "admin/adminNoticeDetail";
		}	
		
		// 공지사항 삭제
		@GetMapping("/admin/adminNoticeDelete/{ocid}")
		public String adminNoticeDeleteGet(HttpSession session,
				@PathVariable Long ocid){
			
			 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
			 if(!adminService.isAdmin(session)) {
				 return "redirect:/";
			 }
			 
			noticeService.deleteNotice(ocid);		
		
			return "redirect:/notice/noticeList";		
		}
}