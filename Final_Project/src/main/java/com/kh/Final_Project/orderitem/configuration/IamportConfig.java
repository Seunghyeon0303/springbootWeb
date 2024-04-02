package com.kh.Final_Project.orderitem.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.siot.IamportRestClient.IamportClient;

@Configuration
public class IamportConfig {

	// 레스트 api key (아임포트)
	@Value("${rest.api.key}")
	private String REST_API_KEY;
	// 시크릿 키 (아임포트)
	@Value("${rest.api.secret}")
	private String REST_API_SECRET;

	@Bean
	public IamportClient iamportClient() {
		return new IamportClient(REST_API_KEY, REST_API_SECRET);
	}
}
