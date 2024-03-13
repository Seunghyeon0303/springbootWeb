package com.kh.board4.restcontroller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kh.board4.entity.Board;
import com.kh.board4.repository.BoardRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class BoardRestController {

	// controller로 restController로는 url 중복 생길 수 있다.
	// 중복 안되게 신경 써야된다.
	// 데이터베이스 접근 해야되니깐 Repository 필요하다.
	@Autowired
	private BoardRepository repository;

	// ResponseEntity<?> 메서드 실행 마다 타입을 지정해서
	// 보낼 수 있고 ResponseEntity<Board> 타입이 고정되서
	// Board타입이 아니면 에러가 난다!

	@GetMapping("/board/detailModal/{id}")
	public ResponseEntity<?> getBoardDetail(@PathVariable("id") Long id) {

		// 1. 로그 출력
		log.info("id: {}", id);

		// 2. 아이디를 기준으로 Board엔티티를 데이터베이스에서
		// 찾는다.
		Board board = repository.findById(id).orElse(null);

		// 조회수를 올려주는 코드

		board.setReadCount(board.getReadCount() + 1);
		repository.save(board);
		log.info("{}", board);

		// 찾는 board가 없는 경우
		if (board == null) {
			// 404 상태 코드와 함께 메시지를 반환
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found id: " + id);
		}
		return ResponseEntity.ok(board);
	}

	// 전체 내용을 가져가는 메서드
	@GetMapping("/board/all")
	public ResponseEntity<?> boardAll() {
		// 모든 Board엔티티를 조회한다.
		List<Board> list = repository.findAll();

		// 조회된 list가 없는 경우
		if (list == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("없는데? 디비확인해라!");
		}
		log.info("board/all : {}", list);

		return ResponseEntity.ok(list);
	}

	@PostMapping("/board/searchModal")
	public ResponseEntity<?> searchModal(@RequestParam("keyword") String keyword) {

		log.info("searchModal() keyword: {}", keyword);
		// 제목 또는 내용에 keyword값이 포함하는 Board엔티티를 DB에서 조회한다.
		List<Board> list = repository.findByTitleContainingOrContentContaining(keyword, keyword);
		// 찾는 board가 없는 경우
		if (list == null) {
			// 404 상태 코드와 함께 메시지를 반환
			return ResponseEntity.status(
					HttpStatus.NOT_FOUND)
					.body("Not found keyword: "
							+ keyword );
		}
		return ResponseEntity.ok(list);
	}
	
	@DeleteMapping("/board/delete/{no}")
	public ResponseEntity<String> deleteBoard(@PathVariable Long no) {
		
		// 데이터가 있는지 조회하고 삭제한다.
		try {
			repository.deleteById(no);
			
			return ResponseEntity.ok("게시물이 성공적으로 삭제되었습니다.");
			
		} catch(Exception e) {
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("게시물 삭제 중 오류가 발생했다!");
		}
	}
}
