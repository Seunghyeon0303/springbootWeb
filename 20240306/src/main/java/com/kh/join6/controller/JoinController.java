package com.kh.join6.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.kh.join6.entity.JoinMember;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class JoinController {

	@GetMapping("/")
	public String join(Model model) {
		log.info("join() 실행");
		
		model.addAttribute("userInfo", new JoinMember());
		
		return "join";
	}
}
