package com.kh.Final_Project.event.service;

import org.springframework.data.domain.*;

import com.kh.Final_Project.event.entity.Event;
import com.kh.Final_Project.event.vo.EventForm;

public interface EventService {

	// 이벤트 전체 조회 
	Page<Event> findAll(Pageable pageable);
	
	// 이벤트 1건 조회
	Event findEventByOcid(Long ocid);
	
	// 이벤트 게시글 수정
	Event edit(Long ocid, EventForm eventVo);
	Event saveEvent(Event event);
	
	// 이벤트 게시글 삭제
	Event deleteEvent(Long ocid);
	
	// 이벤트 게시글 생성
	Event createEvent(Event event);

	// 데이터 데베 저장
	void resetEventData();
	
	// 고객용 // 이벤트 전체
	Page<Event> findAll2(Pageable pageable);	
}