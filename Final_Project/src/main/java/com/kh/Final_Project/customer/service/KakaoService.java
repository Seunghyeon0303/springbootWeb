package com.kh.Final_Project.customer.service;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
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
public class KakaoService {
	
	// 프로퍼티스에서 값을 꺼내와서 변수에 직접 저장
	@Value("${kakao.client.id}")
	private String KAKAO_CLIENT_ID;

	@Value("${kakao.client.secret}")
	private String KAKAO_CLIENT_SECRET;

	@Value("${kakao.redirect.url}")
	private String KAKAO_REDIRECT_URL;

	private final static String KAKAO_AUTH_URI = "https://kauth.kakao.com";

	public Object getKakaoLogin() {
		return KAKAO_AUTH_URI + "/oauth/authorize?" + "client_id=" + KAKAO_CLIENT_ID + "&redirect_uri="
				+ KAKAO_REDIRECT_URL + "&response_type=code";
	}

	// 인가코드를 매개변수로 받아서 실제 내 정보를 받아오는
	// 메서드를 만들기
	public CustomerForm getKakaoInfo(String code) {

		if (code == null) {
			try {
				throw new Exception("인가코드가 오지 않았다!");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 토큰을 받아오면 저장하는 변수
		String accessToken = "";
		// 카카오에서 받아온 데이터를 기준으로 dto를 생성해도 좋다
		CustomerForm dto = null;

		try {

			// HttpHeaders 객체를 생성
			HttpHeaders headers = new HttpHeaders();

			headers.add("Content-type", "application/x-www-form-urlencoded");

			MultiValueMap<String, String> params = new LinkedMultiValueMap<>(); // MultiValueMap 객체 생성
			params.add("grant_type", "authorization_code"); // grant_type 파라미터 추가
			params.add("client_id", KAKAO_CLIENT_ID); // client_id 파라미터 추가
			params.add("client_secret", KAKAO_CLIENT_SECRET); // client_secret 파라미터 추가
			params.add("code", code); // code 파라미터 추가
			params.add("redirect_uri", KAKAO_REDIRECT_URL); // redirect_uri 파라미터 추가

			// 전송할 때 Rest API 편하게 사용할 수있도록 스프링이
			// 지원해주는 RestTemplate클래스를 이용해서
			// HttpEntity ( request 헤더와 바디, response 헤더와 바디)
			// 요청에 따라서 객체를 생성

			RestTemplate restTemplate = new RestTemplate();

			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

			// 위에 파라메터랑 헤더랑 묶어서 HttpEntity로 저장했으니
			// 이제 전송!
			// exchange()
			// Http요청을 보내고 해당 요청에 대한 응답을 받는데 사용하는 메서드

			ResponseEntity<String> response = restTemplate.exchange(KAKAO_AUTH_URI + "/oauth/token", HttpMethod.POST,
					httpEntity, String.class);
			// json타입으로 변환해서 파싱을 할 것이다!
			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObj = (JSONObject) jsonParser.parse(response.getBody());

			accessToken = (String) jsonObj.get("access_token");


		} catch (Exception e) {
			e.printStackTrace();
		}

		dto = getUserInfoWithToken(accessToken);
		return dto;
	}

	private CustomerForm getUserInfoWithToken(String accessToken) {
		String customerName = "";
		String email="";
		String customerId = "";
		String customerPw = "";
		try {
		// 1. HttpHeader 생성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Bearer " + accessToken);
		headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		// 2. HttpHeader를 HttpEntity 담기
		RestTemplate rt = new RestTemplate();

		HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(headers);

		ResponseEntity<String> re = rt.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, httpEntity,
				String.class);

		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObj = (JSONObject) jsonParser.parse(re.getBody());
		JSONObject account = (JSONObject) jsonObj.get("kakao_account");
		JSONObject profile = (JSONObject) account.get("profile");
		
		
		customerName = String.valueOf(profile.get("nickname"));
		email = String.valueOf(account.get("email"));
		customerId = email.substring(0,email.indexOf("@"));
		customerPw = "oauthAPI로그인Kakao"+accessToken; // 나중에 카카오 로그아웃을 위하여 access_token을 Pw에 저장
		
		}catch(Exception e) {}
		
		return CustomerForm.builder().customerEmail(email).
				customerName(customerName).
				customerId(customerId).customerPw(customerPw).build();

	}
	

}
