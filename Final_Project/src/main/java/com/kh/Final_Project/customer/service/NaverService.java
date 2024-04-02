package com.kh.Final_Project.customer.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.kh.Final_Project.customer.vo.CustomerForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NaverService {

	@Value("${naver.client.id}")
	private String NAVER_CLIENT_ID;

	@Value("${naver.client.secret}")
	private String NAVER_CLIENT_SECRET;

	@Value("${naver.redirect.url}")
	private String NAVER_REDIRECT_URL;
	
	// 인가코드를 요청하는 고정된 변수값
	private final static String NAVER_AUTH_URI = "https://nid.naver.com";
	
	/**
	 * 
	 * @return 로그인하는 URL 정보
	 */
	public String getNaverLogin() {


		return NAVER_AUTH_URI + "/oauth2.0/authorize" + "?client_id=" + NAVER_CLIENT_ID + "&redirect_uri="
				+ NAVER_REDIRECT_URL + "&response_type=code";
	}
	
	/**
	 * 
	 * @param accessToken 로그아웃할 사용자의 엑세스 토큰값
	 * @return 엑세스 토큰값을 만료요청하는 URL
	 */
	public boolean naverLogout(String accessToken) {
		
		try {
			
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded");
			
			// 2. 요청파라미터 작성하기
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // MultiValueMap 객체 생성
			params.add("grant_type", "delete"); // grant_type 파라미터 추가
			params.add("client_id", NAVER_CLIENT_ID); // client_id 파라미터 추가
			params.add("client_secret", NAVER_CLIENT_SECRET); // client_secret 파라미터 추가
			params.add("access_token", accessToken); // redirect_uri 파라미터 추가
			params.add("service_provider", "NAVER"); // redirect_uri 파라미터 추가
			// 3. 전송 준비
			RestTemplate restTemplate = new RestTemplate();
			
			// 4.헤더와 파라미터 합치기
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
			
			// 5. httpEntity 전송이 되면 응답받는 객체로 받아준다.
			ResponseEntity<String> response = restTemplate.exchange(NAVER_AUTH_URI + "/oauth2.0/token", HttpMethod.POST,
					httpEntity, String.class);

			// 6. 응답받은 객체를 이용해서 파싱하기
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());
			String result = (String) jsonObj.get("result");

			if(result.equals("success")) {
				
				return true;
			}
			
			
		}catch (Exception e) {}
		
		return false;
	}
	
	
	public CustomerForm getNaverInfo(String code) {

		if (code == null) {
			try {
				throw new Exception("인가코드가 오지 않았습니다.");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}

		// 액세스토근은 만료시간이 존재하기 때문에 발급받은
		// 리프레쉬 토근을 저장해 두고 액세스토근을 갱신하는 형태로
		// 사용한다.

		// accesstoken : 토근 발급 받을 때 사용하는 것!
		String accessToken = "";
		// refreshToken : accesstoken을 갱신할 때 사용하는것!
		String refreshToken = "";
		CustomerForm dto = null;

		try {
			// 1. 헤더만들기
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded");
			
			// 2. 요청파라미터 작성하기
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // MultiValueMap 객체 생성
			params.add("grant_type", "authorization_code"); // grant_type 파라미터 추가
			params.add("client_id", NAVER_CLIENT_ID); // client_id 파라미터 추가
			params.add("client_secret", NAVER_CLIENT_SECRET); // client_secret 파라미터 추가
			params.add("code", code); // code 파라미터 추가
			params.add("redirect_uri", NAVER_REDIRECT_URL); // redirect_uri 파라미터 추가

			// 3. 전송 준비
			RestTemplate restTemplate = new RestTemplate();

			// 4.헤더와 파라미터 합치기
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

			// 5. httpEntity 전송이 되면 응답받는 객체로 받아준다.
			ResponseEntity<String> response = restTemplate.exchange(NAVER_AUTH_URI + "/oauth2.0/token", HttpMethod.POST,
					httpEntity, String.class);

			// 6. 응답받은 객체를 이용해서 파싱하기
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

			accessToken = (String) jsonObj.get("access_token");
			refreshToken = (String) jsonObj.get("refresh_token");

			// 사용자 정보 가져오기
			dto = getUserInfoWithToken(accessToken);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;

	}

	// 사용자 정보를 객체화 시켜서 반환 받기
	private CustomerForm getUserInfoWithToken(String accessToken) {
		String email = "";
		String name = "";
		String mobile = "";
		String customerId = "";
		String customerPw = "";

		// 1. 헤더 생성
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authorization", "Bearer " + accessToken);
			headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

			// HttpEntity를 생성해서 헤더를 담는다.
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

			// 전송하기
			RestTemplate rt = new RestTemplate();

			ResponseEntity<String> response = rt.exchange("https://openapi.naver.com/v1/nid/me", HttpMethod.POST,
					httpEntity, String.class);

			// 응답받은 객체를 이용해서 파싱
			JSONParser p = new JSONParser();
			JSONObject obj = (JSONObject) p.parse(response.getBody());

			JSONObject reObj = (JSONObject) obj.get("response");
			
			email = String.valueOf(reObj.get("email"));
			name = String.valueOf(reObj.get("name"));
			mobile = String.valueOf(reObj.get("mobile")).replace("-", "");
			customerId = email.substring(0,email.indexOf("@"));
			customerPw =  "oauthAPI로그인Naver"+accessToken;
			
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return CustomerForm.builder().customerEmail(email).
				customerName(name).customerPhone(mobile).
				customerId(customerId).customerPw(customerPw).build();
	}
	
	
	
	

}