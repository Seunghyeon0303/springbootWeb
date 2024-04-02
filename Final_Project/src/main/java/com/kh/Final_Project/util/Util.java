package com.kh.Final_Project.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

// 필요한 메서드들 ex) 난수 생성
@Component
public class Util {

	/** 인증 코드 생성 */
	public String generateCode() {
		String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder code = new StringBuilder();
		for (int i = 0; i < 6; i++) {
			int randomIndex = ThreadLocalRandom.current().nextInt(chars.length());
			code.append(chars.charAt(randomIndex));
		}
		return code.toString();
	}

	/** 패스워드 해시화 */
	public static String encodePassword(String rawPassword) {
		return BCrypt.hashpw(rawPassword, BCrypt.gensalt());
	}
	
	/**
	 * @param rawPassword 입력 값
	 * @param hashedPassword 해시화된 값
	 * 
	 *
	 * @return 같으면 true 다르면 false
	 * 
	 */
	public static boolean verifyPassword(String rawPassword, String hashedPassword) {
		return BCrypt.checkpw(rawPassword, hashedPassword);
	}

	
	
	/**
	 * @param dateStr 문자열 날짜 ex) 2020-01-01
	 * @return 문자열을 LocalDateTime 타입으로 변환
	 */
	public static LocalDateTime StrToLDT(String dateStr){
		 LocalDate date = LocalDate.parse(dateStr);
		 LocalDateTime dateTime = date.atTime(LocalTime.MIDNIGHT);
		return dateTime;
	}

}