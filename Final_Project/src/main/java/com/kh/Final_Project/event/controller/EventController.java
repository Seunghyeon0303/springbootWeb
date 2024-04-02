package com.kh.Final_Project.event.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.kh.Final_Project.admin.service.AdminServiceImp;
import com.kh.Final_Project.event.entity.Event;
import com.kh.Final_Project.event.service.EventServiceImp;
import com.kh.Final_Project.event.vo.EventForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/event")
public class EventController {
	
	@Autowired
	private EventServiceImp eventService;	

	@Autowired
	private AdminServiceImp adminService;
	
	// 고객이 볼 이벤트 목록 <완성>
	@GetMapping("/")
	public String event(Model model, Long ocid,
			@RequestParam(defaultValue="0") int page) {
			 
			// 한 화면에 보여지는 기본 글의 수
			int pageSize = 10;
			// 페이징 처리 
			Pageable pageable=PageRequest.of(page,pageSize);
			Page<Event> list = null;
			// 게시글 가져오기
			list = eventService.findAll(pageable);		
			
			model.addAttribute("events",list.getContent());
			model.addAttribute("page",list);
		
		return "event/event";
	}	
	
	// 관리자_이벤트 전체 목록 + 페이징 처리 <완성>
	@GetMapping("/eventList")
	public String eventList(Model model, HttpSession session, 
			@RequestParam(defaultValue="0") int page) { 
		 
		 // 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		 if(!adminService.isAdmin(session)) {
			 return "redirect:/";
		 }
		 
		// 한 화면에 보여지는 기본 글의 수
		int pageSize = 10;
		// 페이징 처리 
		Pageable pageable=PageRequest.of(page,pageSize);
		Page<Event> list = null;
		// 게시글 가져오기
		list = eventService.findAll(pageable);		
		model.addAttribute("events",list.getContent());
		model.addAttribute("page",list);
		return "admin/adminEvent";
	}	
	
	// 관리자_이벤트 게시글 상세페이지 <완성>
	@GetMapping("/admin/adminEventDetail/{ocid}")
	public String adminEventDetail(Model model, HttpSession session,
			@PathVariable("ocid") Long ocid) {
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }
		// 데베에서 데이터 꺼내기
		Event event = eventService.searchByOcid(ocid);		
		
		// 모델에 값 전달
		model.addAttribute("event",event);
		
		return "admin/adminEventDetail";
	}
	
	// 관리자_이벤트 게시글 수정페이지 (수정 전 내용을 보여주는 메서드) <완성>
	// 먼저 뷰 페이지로 이동
	@GetMapping("/admin/adminEventEdit/{ocid}")
	public String adminEventEditGet(Model model, HttpSession session, 
			@PathVariable("ocid") Long ocid) {
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		 if(!adminService.isAdmin(session)) {
			 return "redirect:/";
		 }
		
		// 수정할 글 데베에서 조회후 해당 1건의 글 보기
		Event updateEvent = eventService.findEventByOcid(ocid);
		
		// 찾은 결과 모델에 담아서 뷰페이지로 이동
		model.addAttribute("event",updateEvent);
		
		return "admin/adminEventEdit";	//수정 페이지로 이동
	}
	
	// 관리자_이벤트 게시글 수정페이지 (수정 후 내용을 위한 메서드) <완성>
	@PostMapping("/admin/adminEventDetail/{ocid}")
	public String adminEventEditPost(
			 EventForm eventVo, HttpSession session, 
			@PathVariable("ocid") Long ocid,Model model) {
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		 if(!adminService.isAdmin(session)) {
			 return "redirect:/";
		 }
		
		// 수정을 위해 데베 등록 글을 가져온다.
		Event updateEvent = eventService.edit(ocid, eventVo);
				
		
		if(updateEvent != null) {
			updateEvent.setTitle(eventVo.getTitle());
			updateEvent.setContent(eventVo.getContent());

			// 저장, 수정, 변경
			Event event = eventService.saveEvent(updateEvent);
			model.addAttribute("event",event);
		}		
		return "admin/adminEventDetail";
	}	
	
//	@PostMapping("/event/admin/adminEventDelete/{ocid}")
//	public String adminEventDeletePost(@PathVariable Long ocid, EventForm eventVo){
//		log.info("eventController adminEventDeletePost()");
//		Event deleteEvent = eventService.deleteEvent(ocid,eventVo);		
//		
//		return deleteEvent != null ? 
//				"admin/adminEventDetail" : 
//					"admin/adminEvent" ;	 	
//	}
	
	// 이벤트 삭제 <완성>
	@GetMapping("/admin/adminEventDelete/{ocid}")
	public String adminEventDeleteGet(
			HttpSession session,
			@PathVariable Long ocid){
		log.info("eventController adminEventDeleteGet()");

		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		 if(!adminService.isAdmin(session)) {
			 return "redirect:/";
		 }
		 
		eventService.deleteEvent(ocid);		
	
		return "redirect:/event/eventList";		
	}
	
	 // 이벤트 게시글 작성 (작성 전, 저장 전) <완성>
	@GetMapping("/admin/adminEventNew")
	public String adminEventNew(HttpSession session) {
		log.info("eventController adminEventNew()");		
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
		 if(!adminService.isAdmin(session)) {
			 return "redirect:/";
		 }
		
		return "admin/adminEventNew";
	}
	
	// 이벤트 게시글 작성 (작성 후, 저장) <완성>
	@PostMapping("/admin/adminEventCreate")
	public String adminEventCreate(EventForm eventVo,Model model,
			HttpSession session) {
		log.info("eventController adminEventCreate()");		
		log.trace(eventVo.toString());
		
		// 고객이 URL 입력해서 들어오는 경우, 고객용 페이지로 이동!
				 if(!adminService.isAdmin(session)) {
					 return "redirect:/";
				 }
		
		// Vo 데이터를 entity로 변경
		Event event = eventVo.toEntity();
		
		// 저장 
		Event saveEvent = eventService.createEvent(event);
		
		model.addAttribute("event",saveEvent);
		log.info("eventController adminEventCreate()_save");		
		
		return "admin/adminEventDetail";
	}
}
