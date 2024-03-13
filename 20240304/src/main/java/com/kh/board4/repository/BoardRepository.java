package com.kh.board4.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kh.board4.entity.Board;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

	// 페이징 처리를 위해서 jpa작성
	Page<Board> findAll(Pageable pageable);

	// title검색
	Page<Board> findByTitleContaining(String keyword, Pageable pageable);

	// content검색
	Page<Board> findByContentContaining(String keyword, Pageable pageable);

	// title + content검색
	Page<Board> findByTitleContainingOrContentContaining(String titlekeyword, String contentkeyword, 
																					Pageable pageable);

	List<Board> findByTitleContainingOrContentContaining(String titlekeyword, String contentkeyword);

}
