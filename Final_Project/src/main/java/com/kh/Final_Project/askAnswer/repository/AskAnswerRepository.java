package com.kh.Final_Project.askAnswer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kh.Final_Project.askAnswer.entity.AskAnswer;
@Repository
public interface AskAnswerRepository extends JpaRepository<AskAnswer, Long>{
	
	List<AskAnswer> findByBoardNo(Long ocid);

	// 0312 답변 삭제
	void deleteByBoardNo(Long boardNo);
	
}