package com.kh.Final_Project.util;

import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.service.CustomerServiceImp;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/util")
public class UtilController {

	@Autowired
	private UtilService service;

	@Autowired
	private CustomerServiceImp customerServiceImp;

	@PostMapping("/sendEmail")
	public ResponseEntity<String> sendEmail(@RequestBody String to, HttpServletResponse response) throws Exception {

		String verificationCode = "";

		verificationCode = service.sendEmail(to, 0, response);

		System.out.println("인증코드 : " + verificationCode);

		return ResponseEntity.ok().body(verificationCode);
	}

	@PostMapping("/verifyCode")
	public ResponseEntity<String> verifyCode(@RequestBody String userInputCode,
			@CookieValue("verificationCode") String cookieCode, HttpServletResponse response) {

		boolean check = service.checkCode(userInputCode, cookieCode, response);

		if (check) {
			return ResponseEntity.ok("인증 성공");
		} else {
			return ResponseEntity.badRequest().body("인증 실패");
		}

	}

	@PostMapping("/sendEmailFindId")
	public ResponseEntity<?> sendEmailFindId(@RequestBody String to, HttpServletResponse response, HttpSession session)
			throws Exception {

		String id = customerServiceImp.findIdByEmail(to);

		if (id == null) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "존재하지 않는 이메일 입니다."));
		}

		String verificationCode = "";

		verificationCode = service.sendEmail(to, 1, response);

		session.setAttribute("userId", id);

		System.out.println("인증코드 : " + verificationCode);

		return ResponseEntity.ok().body(Map.of("success", true, "message", "이메일이 발송되었습니다."));
	}

	@PostMapping("/verifyCodeFindId")
	public ResponseEntity<?> verifyCodeFindId(HttpSession session, @RequestBody String userInputCode,
			@CookieValue("verificationCode") String cookieCode, HttpServletResponse response) {
		boolean check = service.checkCode(userInputCode, cookieCode, response);
		if (check) {
			String userId = ""; // 여기에 userId 값을 설정

			userId = (String) session.getAttribute("userId");
			session.invalidate();

			StringBuilder maskedId = new StringBuilder(userId);

			Random r = new Random();

			for (int i = 0; i < userId.length() / 2; i++) {
				int ranInt = r.nextInt(userId.length());
				maskedId.setCharAt(ranInt, '*');
			}

			userId = maskedId.toString();

			return ResponseEntity.ok(Map.of("success", true, "userId", userId));

		} else {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "인증 실패"));
		}
	}
	
	// 비번 발송
	@PostMapping("/sendEmailChangePw")
	public ResponseEntity<?> sendEmailChangePw(@RequestBody EmailRequest request, HttpServletResponse response,HttpSession session) throws Exception {
		
		String userId = request.getUserId();
		
		String to = request.getEmail();
		
		String id = customerServiceImp.findIdByEmail(to);
		
		if(id==null) {
			return ResponseEntity.ok().body(Map.of("success",false,"message","존재하지 않는 이메일 입니다."));
		}else if(!id.equals(userId)) {
			return ResponseEntity.ok().body(Map.of("success",false,"message","일치하지 않은 이메일입니다."));
		}
		
		String verificationCode = "";
		
		verificationCode = service.sendEmail(to,2,response);
		
		session.setAttribute("userId", id);
		
		System.out.println("인증코드 : "+verificationCode);
		
		 return ResponseEntity.ok().body(Map.of("success", true, "message", "이메일이 발송되었습니다."));
	}
	
	@PostMapping("/userIdCheck")
	public ResponseEntity<?> userIdCheck(@RequestBody String id){
		log.info("userIdCheck에서 받아온 id : {}",id);
		
		boolean result = customerServiceImp.findById(id);
		
		if(result) {
			return ResponseEntity.status(HttpStatus.OK).build();
		}else {
			return ResponseEntity.status(HttpStatus.BAD_GATEWAY).build();
		}
		
	}
	
	
	

}
