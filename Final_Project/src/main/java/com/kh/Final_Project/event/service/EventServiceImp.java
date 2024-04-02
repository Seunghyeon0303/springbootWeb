package com.kh.Final_Project.event.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.event.entity.Event;
import com.kh.Final_Project.event.repository.EventRepository;
import com.kh.Final_Project.event.vo.EventContent;
import com.kh.Final_Project.event.vo.EventForm;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.product.entity.Product.Category;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class EventServiceImp implements EventService {

	@Autowired
	private EventRepository eventRepository;

	// 이벤트 전체 조회
	@Override
	public Page<Event> findAll(Pageable pageable) {

		Page<Event> list = eventRepository.findAll(pageable);

		List<EventContent> eventList = new ArrayList<>();

		for (Event temp : list) {

			// 새 객체 생성
			EventContent eventcontent = new EventContent();
			// EventContent 객체에 제목 설정
			eventcontent.setTitle(temp.getTitle());

			// EventContent에 리스트 타입으로 내용 넣기
			List<String> tempContent = Arrays.asList(temp.getContent().split("\\n"));
			eventcontent.setContent(tempContent);

			eventList.add(eventcontent);
		}
		return eventRepository.findAll(pageable);
	}

	// 이벤트 1건 조회
	@Override
	public Event findEventByOcid(Long ocid) {
		return eventRepository.findById(ocid).orElse(null);
	}

	// 이벤트 수정 (저장 전)
	@Override
	public Event edit(Long ocid, EventForm eventVo) {

		Event event = eventVo.toEntity();
		Event targetEvent = eventRepository.findById(ocid).orElse(null);

		if (targetEvent == null || ocid != event.getOcid()) {
			return null;
		}

		if (event.getTitle() == null && event.getContent() == null) {
			return null;
		}
		targetEvent.patch(event);

		return targetEvent;
	}

	// 이벤트 수정 (후 ~ 저장)
	@Override
	public Event saveEvent(Event event) {
		return eventRepository.save(event);
	}

	// 이벤트 게시글 삭제
	@Override
	public Event deleteEvent(Long ocid) {

		Event targetEvent = eventRepository.findById(ocid).orElse(null);

		if (targetEvent == null) {
			return null;
		}
		eventRepository.delete(targetEvent);
		return targetEvent;
	}

	// 이벤트 게시글 생성
	@Override
	public Event createEvent(Event event) {
		return eventRepository.save(event);
	}

	// 이벤트 게시글 내용 데이터
	public long countEvent() {
		return eventRepository.count();
	}

	// 이벤트 게시글 내용 입력 데이터
	@Override
	public void resetEventData() {
		if (countEvent() == 0) { // 데이터베이스가 비어있는 경우에만 삽입
			List<Event> events = new ArrayList<>();
			// 이벤트 데이터 추가
			events.add(new Event("과즙이 주르륵\n 입에서 사르륵", 
					"3월의 제철 과일을 담아보세요\n\n #딸기 #한라봉 #매실 #대저 토마토\n\n",
					"상품 보러 가기",
					"/img/event/03fruit.jpg"));
			events.add(new Event("매일 마주하는 신선함\n신선 보장!",
					"3월의 제철 채소를 담아보세요\n\n #쑥 #달래 #냉이 #취나물\n\n#씀바귀 #더덕 #우엉\n\n",
					"상품 보러 가기",
					"/img/event/03vegetable.jpg"));
			events.add(new Event("지금 놓치면\n내년에 만나요", 
					"3월의 제철 해산물을 담아보세요\n\n #소라 #꼬막 #주꾸미 #바지락 #도미\n\n",
					"상품 보러 가기",
					"/img/event/03seafood.jpg"));
			events.add(new Event("고기는 항상\n옳다!", 
					"이런 메뉴는 어떠세요?\n\n #소고기 #돼지고기 #닭고기 #양고기 #오리고기\n\n",
					"상품 보러 가기",
					"/img/event/03meat.jpg"));
			events.add(new Event("Simple is Best\n간편하게 즐기는 식사", 
					"밀키트로 간편하지만 영양 가득한 식사를 위해 담아보세요\n\n",
					"상품 보러 가기",
					"/img/event/03mealkit.jpg"));

			eventRepository.saveAll(events);

		}
	}

	// 이미지 업로드 관련
	public Event searchByOcid(Long ocid) {

		Optional<Event> eventOpt = eventRepository.findById(ocid);

		if (eventOpt.isEmpty()) {
			return null;
		}

		return eventOpt.get();
	}

	@Override
	public Page<Event> findAll2(Pageable pageable) {

		Page<Event> list = eventRepository.findAll(pageable);

		List<EventContent> eventList = new ArrayList<>();

		for (Event temp : list) {

			// 새 객체 생성
			EventContent eventcontent = new EventContent();
			// EventContent 객체에 제목 설정
			eventcontent.setTitle(temp.getTitle());

			// EventContent에 리스트 타입으로 내용 넣기
			List<String> tempContent = Arrays.asList(temp.getContent().split("\n"));
			eventcontent.setContent(tempContent);

			eventList.add(eventcontent);
		}
		return eventRepository.findAll(pageable);
	}

//	public List<List<Event>> eventListShow(List<Event> eventList) {
//		List<List<Event>> eventShowList = new ArrayList<>();
//		List<Event> eventChunk = new ArrayList<>(5);
//		
//		for(Event event : eventList) {
//			eventChunk.add(event);
//			if(eventChunk.size() == 5) {
//				eventShowList.add(new ArrayList<>(eventChunk));
//				eventChunk.clear();
//			}
//		}
//		
//		if(!eventChunk.isEmpty()) {
//			eventShowList.add(eventChunk);
//		}
//		
//		return eventShowList;
//	}		
}