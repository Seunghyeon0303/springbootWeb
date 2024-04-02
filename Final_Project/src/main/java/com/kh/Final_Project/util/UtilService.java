package com.kh.Final_Project.util;


import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UtilService {

	@Autowired
	private Util util;

	@Autowired
	private JavaMailSender mailSender;
	
	/**
	 * 이메일 발송 메서드
	 * 
	 * @param to 이메일 주소
	 * @param emailType 이메일 유형 (0: 회원가입, 1: 아이디 찾기, 2: 비밀번호 변경)
	 * @param response HttpServletResponse 객체
	 * @return 인증코드
	 * @throws Exception 예외 처리
	 */
	public String sendEmail(String to, int emailType, HttpServletResponse response) throws Exception {
	    MimeMessage message = mailSender.createMimeMessage();
	    String verificationCode = util.generateCode();
	    String msg = "";
	    
	    switch(emailType) {
	        case 0: // 회원가입
	            msg += "<h1>회원가입 이메일 인증</h1>";
	            msg += "<p>아래의 인증 코드를 회원가입 창에서 입력해 주세요.</p>";
	            message.setSubject("GreenBox 회원가입 인증"); // 이메일 제목
	            break;
	        case 1: // 아이디 찾기
	            msg += "<h1>아이디 찾기 이메일 인증</h1>";
	            msg += "<p>아래의 인증 코드를 아이디 찾기 창에서 입력해 주세요.</p>";
	            message.setSubject("GreenBox 아이디 찾기 본인 인증"); // 이메일 제목
	            break;
	        case 2: // 비밀번호 변경
	            msg += "<h1>비밀번호 변경 이메일 인증</h1>";
	            msg += "<p>아래의 인증 코드를 비밀번호 변경 창에서 입력해 주세요.</p>";
	            message.setSubject("GreenBox 비밀번호 변경"); // 이메일 제목
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid email type: " + emailType);
	    }
	    
	    msg += "<div><h2>" + verificationCode + "</h2></div>";
	    
	    message.addRecipients(RecipientType.TO, to); // 보내는 대상	
	    
	    message.setText(msg, "utf-8", "html"); // 이메일 내용
	    message.setFrom(new InternetAddress("bobbo01@naver.com", "GreenBox")); // 보내는 사람
	    
	    mailSender.send(message); // 이메일 전송

	    String hashedVerificationCode = Util.encodePassword(verificationCode); // 인증 코드 해싱
	    
	    // 쿠키에 인증 코드 저장
	    Cookie cookie = new Cookie("verificationCode", hashedVerificationCode);
	    cookie.setMaxAge(3 * 60); // 쿠키 유효 시간 설정 (예: 3분)
	    cookie.setHttpOnly(true); // JavaScript를 통한 쿠키 접근 방지
	    response.addCookie(cookie); // 응답에 쿠키 추가
	    
	    return verificationCode;
	}


	/**
	 * @param userInputCode 사용자가 입력한 숫자
	 * @param encodedVerificationCode 쿠키에  저장된 인코딩된 인증번호
	 * @return 쿠키에 저장된 인증코드를 디코딩 후 사용자가 입력한 숫자와 비교 후 맞으면 true 틀리면 false
	 */
	public boolean checkCode(String userInputCode, String encodedVerificationCode, HttpServletResponse response) {
		boolean check =  Util.verifyPassword(userInputCode, encodedVerificationCode);
		if (check) { // 인증코드가 같으면 쿠키 삭제
			Cookie cookie = new Cookie("verificationCode", null);
			cookie.setPath("/");
			cookie.setMaxAge(0);
			response.addCookie(cookie);
			return true;
		} else {
			return false;
		}
	}

}