package com.kh.join6.restcontroller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.kh.join6.entity.JoinMember;
import com.kh.join6.repository.JoinMemberRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class JoinRestController {

	// 자바스크립트에서 비동기 통신을 할 때 url
	// restcontroller 매핑 시켜야된다.
	
	// 실제 넘어오는 코드가 json타입 json -> map 형식으로 받아서 저장할 수 있다.
	// 만약 여러개 데이터가 넘어온다면 클래스타입으로 받아주면 된다.
	
	@Autowired
	private JoinMemberRepository repository;
	
	// 가입하기
	@PostMapping("/signup")
	public ResponseEntity<String> singup(@RequestBody JoinMember member) {
		// 3. log 출력
		log.info("signup! {}", member);
		
		// 4. 레퍼지토리 이용해서 저장
		JoinMember result = repository.save(member);
		
		// 5. 자바스크립트로 결과 보내기
		if(result != null) {
			// 정상적으로 데이터베이스에 저장했다.
			return ResponseEntity.ok("정상적으로 가입 되었습니다.");
		} else {
			// 가입하지 못했을 경우 null 값이 온다.
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("가입하지 못했습니다!");
		}	
	}
	
	// 중복확인
	@PostMapping("/check-duplicate")
	public ResponseEntity<String> checkDuplicate(@RequestBody Map<String, String> body) {
		
		log.info("checkDuplicate(");
		log.info("넘어온 결과 {}", body);
		
		// 1. body에서 아이디값 꺼내기
		String userId = body.get("userId");
		
		// 2. 래퍼지토리 이용해서 데이터 검색
		JoinMember member = repository.findByEmail(userId);
		
		log.info("db 실행 결과 : {}", member);
		
		// 3. 결과 보내기!
		if(member != null) {
			// 중복되는 아이디가 있다!
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"message\" : \"아이디는 이미 사용 중 입니다.\"}");
			
		} else {
			// 중복되는 아이디가 없다! 사용 가능하다.
			return ResponseEntity.ok("{\"message\" : \"아이디는 사용 가능합니다.\"}");
		}
	}
}
