package com.kh.Final_Project.notice.service;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.notice.entity.NoticeEntity;
import com.kh.Final_Project.notice.repository.NoticeRepository;
import com.kh.Final_Project.notice.vo.NoticeForm;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NoticeServiceImp implements NoticeService{

	@Autowired
	private NoticeRepository noticeRepository;

	// 공지사항 전체 조회 
	@Override
	public Page<NoticeEntity> findAll(Pageable pageable) {
		return noticeRepository.findAll(pageable);
	}

	// 공지사항 1건 조회
	@Override
	public NoticeEntity findNoticeByOcid(Long ocid) {
		return noticeRepository.findById(ocid).orElse(null);
	}
	
	// 공지사항 게시글 생성
	@Override
	public NoticeEntity createNotice(NoticeEntity notice) {
		return noticeRepository.save(notice);
	}

	// 공지사항 수정 (저장 전)
	@Override
	public NoticeEntity edit(Long ocid, NoticeForm noticeVo) {
		
		NoticeEntity notice = noticeVo.toEntity();
		NoticeEntity targetNotice = noticeRepository.findById(ocid).orElse(null);
		
		if(targetNotice == null || ocid != notice.getOcid()) {
			return null;
		}
		
		if(notice.getTitle() == null && notice.getContent() == null) {
			return null;
		}
			targetNotice.patch(notice);
			
			return targetNotice;		
	}

	// 공지사항 수정 (후 ~ 저장)
	@Override
	public NoticeEntity saveNotice(NoticeEntity notice) {
		return noticeRepository.save(notice);
	}

	// 공지사항 게시글 삭제
	@Override
	public NoticeEntity deleteNotice(Long ocid) {
		
		NoticeEntity targetNotice = noticeRepository.findById(ocid).orElse(null);
		
		if(targetNotice == null) {
			return null;
		}
		
		noticeRepository.delete(targetNotice);
		
		return targetNotice;
	}

	// 공지사항 게시글 내용 데이터
	public long countNotice() {
		return noticeRepository.count();
	}
	
	// 공지사항 게시글 내용 입력 데이터
	@Override
	public void resetNoticeData() {
		if (countNotice() == 0) { // 데이터베이스가 비어있는 경우에만 삽입
			log.info("공지사항 데이터 비어있음");
			List<NoticeEntity> notices = new ArrayList<>();
			// FAQ 데이터 추가
			
			// (1)
			notices.add(new NoticeEntity("[안내] 카카오페이 서비스 점검 안내 (2024.01.01)",
					"안녕하세요.\n\n" +
							"GreenBox입니다.\n\n\n" +
							"해당 결제 서비스의 시스템 점검으로 이용이 일시적으로 불가할 수 있습니다.\n" +
							"아래 내용 확인하시어 이용에 불편 없으시기 바랍니다.\n\n\n" +
							"# 일시 : 2024년 1월 1일 오전 01:00~06:00\n\n" +
							"# 작업 내용 : 카카오페이 서비스 시스템 점검\n\n" +
							"# 영향 : 카카오페이 모든 서비스 중단\n\n\n" +
							"※ 시스템 작업 진행 상황에 따라 중단 시간은 변경될 수 있습니다.\n\n\n" +
					"감사합니다.\n\n"));
			
			// (2)
			notices.add(new NoticeEntity("[안내] GreenBox 서비스 점검 안내 (2024.01.11)",
					"안녕하세요.\n\n" +
					"GreenBox입니다.\n\n\n" +
					"보다 안정적인 서비스 제공을 위해 서버 점검을 진행할 예정입니다.\n" +
					"점검 시간 동안 서비스 사용이 중단될 예정입니다.\n" +
					"고객님의 양해 부탁드립니다.\n\n\n" +
					"# 일시 : 2024년 1월 11일 오전 01:00~06:00\n\n" +
					"# 작업 내용 : GreenBox 서비스 시스템 점검\n\n" +
					"# 영향 : GreenBox의 모든 서비스 중단\n\n\n" +
					"※ 시스템 작업 진행 상황에 따라 중단 시간은 변경될 수 있습니다.\n\n\n" +
					"감사합니다.\n\n"));
			
			// (3)
			notices.add(new NoticeEntity("[안내] SKT 휴대폰 결제 서비스 점검 안내(2024.01.21)",
					"안녕하세요.\n\n" +
							"GreenBox입니다.\n\n\n" +			
							"해당 결제 서비스의 시스템 점검으로 인하여 이용이 일시적으로 불가할 수 있습니다.\n" +
							"아래 내용을 확인하시어 이용에 불편 없으시기 바랍니다.\n\n\n" +
							"# 일시 : 2024년 1월 21일 오전 01:00~06:00\n\n" +
							"# 작업 내용 : SKT 휴대폰 결제 서비스 시스템 점검\n\n" +
							"# 영향 : GreenBox의 모든 서비스 중단\n\n\n" +
							"※ 시스템 작업 진행 상황에 따라 중단 시간은 변경될 수 있습니다.\n\n\n" +
					"감사합니다.\n\n"));
			
			// (4)
			notices.add(new NoticeEntity("2024년 4월부터 멤버십 쿠폰이 자동 발급됩니다.",
					"웹사이트를 방문하여 원하는 상품을 검색합니다.\n\n" 
							+ "상품의 상세 페이지에서 수량을 선택하고 '장바구니에 추가' 버튼을 클릭하세요.\n\n"
							+ "장바구니 페이지에서 주문 내역을 확인하고, '구매하기'를 클릭해 결제 페이지로 이동합니다.\n\n"
							+ "결제 정보를 입력하고, 주문을 확인한 뒤 '주문 완료' 버튼을 클릭합니다.\n\n" 
							+ "주문이 성공적으로 처리되면, 주문확인 이메일을 받게 됩니다.\n\n\n" 
							
							+ "이메일에는 주문 번호, 주문 상품 정보, 배송 예정일 등이 포함됩니다.\n\n"
							+ "주문 상태는 언제든지 웹사이트에서 확인할 수 있으며, 변경사항이 있을 경우 이메일로 알려드립니다.\n\n"
							+ "주문 과정에서 문제가 발생한 경우, 고객센터로 문의해 주세요.\n\n" 
							+ "고객님의 만족을 위해 항상 최선을 다하겠습니다.\n\n\n" 
							
							+ "고객센터 : 1577-7813\n\n" 
							+ "이용 가능시간 : 월~금 오전 10시~오후 6시\n\n" 
							+ "점심시간 : 오후 1~2시\n\n"
					));		
			
			noticeRepository.saveAll(notices);

		} else {
			log.info("공지사항 데이터 있음");
		}
	}

	
	
}