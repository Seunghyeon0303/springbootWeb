package com.kh.Final_Project.faq.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import com.kh.Final_Project.faq.entity.FAQEntity;
import com.kh.Final_Project.faq.repository.FAQRepository;
import com.kh.Final_Project.faq.vo.FAQForm;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FAQServiceImp implements FAQService {

	@Autowired
	private FAQRepository faqRepository;

	@Override
	public Page<FAQEntity> findAll(Pageable pageable) {
		return faqRepository.findAll(pageable);
	}

	// @Override
	public List<FAQEntity> findByCategory(String category) {
		return faqRepository.findByCategory(category);
	}

	@Override
	public FAQEntity findFaqByOcid(Long ocid) {
		return faqRepository.findById(ocid).orElse(null);
	}

	@Override
	public FAQEntity edit(Long ocid, FAQForm faqVo) {
		FAQEntity faq = faqVo.toEntity();
		FAQEntity targetFaq = faqRepository.findById(ocid).orElse(null);

		if (targetFaq == null || ocid != faq.getOcid()) {
			return null;
		}

		if (faq.getTitle() == null && faq.getContent() == null) {
			return null;
		}
		targetFaq.patch(faq);

		return targetFaq;
	}

	@Override
	public FAQEntity saveFaq(FAQEntity faq) {
		return faqRepository.save(faq);
	}

	@Override
	public FAQEntity deleteFaq(Long ocid) {

		FAQEntity targetFaq = faqRepository.findById(ocid).orElse(null);

		if (targetFaq == null) {
			log.info("잘못된요청!{}번 글을 존재하지 않아요", ocid);
			return null;
		}
		faqRepository.delete(targetFaq);
		return targetFaq;

	}

	@Override
	public FAQEntity createFaq(FAQEntity faq) {
		return faqRepository.save(faq);
	}

	// FAQ 게시글 내용 데이터
	public long countFaq() {
		return faqRepository.count();
	}

	// FAQ 게시글 내용 입력 데이터
	@Override
	public void resetFaqData() {
		if (countFaq() == 0) { // 데이터베이스가 비어있는 경우에만 삽입
			List<FAQEntity> faqs = new ArrayList<>();
			// FAQ 데이터 추가
			faqs.add(new FAQEntity("주문 방법은 어떻게 되나요?",
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
							+ "점심시간 : 오후 1~2시\n\n",
					"주문/배송"));
			
			faqs.add(new FAQEntity("전화로 주문 할 수 있나요?",
					"전화(유선상) 주문 가능합니다.\n\n" +
					"- 고객센터 1577-7813로 연락주시면 주문이 가능합니다.\n\n" +
					"- 결제방법은 계좌이체로만 가능합니다.\n\n",
					"주문/배송"));

			faqs.add(new FAQEntity("주문 내역 조회는 어디서 하나요?",
					"# 주문 내역 조회\n\n" +
					"- 하기 경로를 통해 구매하신 주문건의 이력을 확인할 수 있습니다.\n\n" +
					"로그인 - 홈 상단 [회원명] 클릭 - 주문 내역\n\n",
					"주문/배송"));

			faqs.add(new FAQEntity("대량주문은 어떤 방법으로 주문하나요?",
					"# 대량주문 접수방법\n\n" +
					"- 상품 대량주문 접수 및 문의\n\n" +
					"'1:1 문의하기'에 문의를 남겨주실 경우, 확인 후 개별 상담을 통해 주문이 진행 됩니다.\n\n",
					"주문/배송"));

			faqs.add(new FAQEntity("배송지역 검색 시, 배송 불가지역으로 조회됩니다.",
					"# 배송불가 지역 안내\n\n" +
					"- 신도시/신규 지번 등으로 일부 배송 불가지역이 확인 될 수 있으며 이런 경우 1:1게시판으로 배송불가 지역의 주소를 함께 기재하여 문의 남겨주시기를 부탁드립니다.\n\n" +
					"- GreenBox에서 사용하는 하루배송(택배)은 당일 출고, 당일 배송하는 형태로서 만일 당일배송을 지원하지 않는 지역은 배송이 불가합니다.\n\n" +
					"(ex. 주문 후, 다음다음날 배송되는 지역 X )\n\n",
					"주문/배송"));

			faqs.add(new FAQEntity("친환경/유기농/무농약이 어떻게 다른가요?",
					"# 친환경 인증이란?\n\n" +
					"친환경 농산물은 인체와 생태환경에 해로운 화학비료, 농약, 제초제 등을 최대한 사용하지 않고 재배한 농산물을 일컬으며 이 중, 화학비료와 농약 사용량에 따라 저농약, 무농약, 유기농으로 구분됩니다\n\n" +
					"- 저농약 : 일반 농산물 대비 적은 양의 농약을 사용한 농산물\n\n" + 
					"- 무농약 : 농약은 사용하지 않되, 화학비료를 사용한 농산물\n\n" +
					"- 유기농 : 화학비료와 농약을 일체 사용하지 않고 재배한 농산물\n\n",
					"상품"));

			faqs.add(new FAQEntity("회원 가입시 어떠한 혜택이 있나요?",
					"# 회원가입 혜택\n\n" +
					"- GreenBox에서 진행하는 다양한 이벤트를 적용받을 수 있습니다.\n\n" +
					"- GreenBox 멤버십 등급에 따라 할인율이 차등 적용됩니다.\n\n" +
					"- 신규가입 시에는 첫 주문 당일과 첫주문 완료 후 한달 동안 무료배송 혜택이 주어지며, 신규회원 대상의 쿠폰 혜택도 주어집니다.\n\n",
					"회원가입/정보"));

			faqs.add(new FAQEntity("회원정보 변경은 어떻게 하나요?",
					"# 회원정보변경 방법 안내\n\n" +
					"- 아래 경로를 통해 탈퇴하실 수 있습니다.\n\n" +
					"홈 상단 [회원명] - 마이페이지 - 개인정보 수정 - 비밀번호 재확인\n\n" +
					"- 비밀번호 인증 후 개인정보 변경 가능합니다.\n\n",
					"회원가입/정보"));

			faqs.add(new FAQEntity("아이디, 비밀번호를 잊어버렸습니다.",
					"# 아이디, 비밀번호 찾기 안내\n\n" +
					"- 하기 경로를 통해 아이디 및 비밀번호 찾기가 가능하며, 임시 비밀번호의 경우 회원가입 시 등록한 이메일 주소로 발송 됩니다.\n\n" +
					"홈 상단 [로그인] - 화면 아래 <아이디 찾기> 또는 <비밀번호 변경>\n\n\n" +
					"[참고]\n\n" +
					"** 가입시 기재한 메일 주소가 기억나지 않으시거나 오류가 발생하는 경우 고객센터로 문의 바랍니다.\n\n" +
					"** 상담시에는 고객님의 개인정보보호를 위해 기존에 사용하시던 비밀번호는 안내가 불가합니다.\n\n" +
					"개인정보 확인 후 임시비밀번호를 설정해드립니다.\n\n",
					"회원가입/정보"));

			faqs.add(new FAQEntity("회원 탈퇴를 하고 싶습니다.",
					"# 탈퇴 방법 안내\n\n" +
					"- 아래 경로를 통해 탈퇴하실 수 있습니다.\n\n" +
					"홈 상단 [회원명] - 마이페이지 -\n개인정보 수정 - 비밀번호 재확인 - 최하단 [탈퇴하기] 버튼\n\n\n" +
					"[참고]\n\n" +
					"** 고객님의 개인정보를 위해 직접 탈퇴만 가능합니다.\n\n" +
					"** 잔여 쿠폰은 탈퇴와 함께 소멸되며 별도의 환불처리는 불가합니다.\n\n",
					"회원가입/정보"));
			
			faqs.add(new FAQEntity("주문을 취소하고 싶습니다.",
                    "# 주문취소 방법 안내\n\n" +
                    "- 주문 취소는 배송 단계별로 상이하오니 아래 내용 참고 바랍니다.\n\n" +
                    "ㄴ[주문완료] : 마이페이지 - 주문내역 - 주문번호 클릭 - 상세페이지 맨 하단에서 [직접취소] 진행\n\n" +
                    "ㄴ[배송준비중] 이후 : 고객센터로 문의 바랍니다.\n\n\n" +
                    "[참고]\n\n" +
                    "** 배송이 시작 된, [배송준비중] 이후 단계에서는 취소가 일부 제한되는 점 고객님의 양해 부탁드립니다.\n\n" +
                    "** 주문상품의 부분 취소는 불가합니다.\n\n" +
                    "** 전체 취소 이후, 재구매 바랍니다.\n\n",
                    "취소/교환/환불"));

			faqs.add(new FAQEntity("환불 안내는 받았는데, 아직 카드사에서는 취소가 안되었어요.",
                    "카드 취소 시, 유의사항\n\n" +
                    "- 최종 카드 환불의 경우 카드사 사정으로 인해 환불접수일로부터 영업일 기준 3~5일 가량 소요 될 수 있습니다.\n\n" +
                    "- 취소 완료 시, 카드사에서 안내드리는 SMS는 고객님의 수신 설정여부에 따라 미수신 될 수 있습니다.\n\n\n" +
                    "[참고]\n\n" +
                    "** 자세한 사항은 카드사에 문의 부탁드리며, 7일 이후에도 환불이 되지 않은 경우 고객센터로 문의 부탁드립니다.\n\n",
                    "취소/교환/환불"));

			faqs.add(new FAQEntity("단순변심/주문착오로 인한, 교환(반품) 기준이 궁금합니다.",
                    "단순 변심, 주문 착오의 경우\n\n" +
                    "- 재판매가 불가한 상품의 특성상, 단순 변심, 주문 착오 시 교환 및 반품이 어려운 점 양해 부탁드립니다.\n\n" +
                    "- 상품에 따라 조금씩 맛이 다를 수 있으며, 개인의 기호에 따라 같은 상품도 다르게 느끼실 수 있습니다.\n\n\n" +
                    "※ 단순 변심으로 인한 교환 또는 환불의 경우 고객님께서 배송비 3,000원을 부담하셔야 합니다.\n\n",
                    "취소/교환/환불"));

			faqs.add(new FAQEntity("교환(반품)이 불가한 경우도 있나요?",
					"# 교환·반품이 불가한 경우\n\n" +
					"다음에 해당하는 교환·환불 신청은 처리가 어려울 수 있으니 양해 부탁드립니다.\n\n" + 
					"- 고객님의 책임 있는 사유로 상품이 멸실되거나 훼손된 경우\n" +
					"(단, 상품의 내용을 확인하기 위해 포장 등을 훼손한 경우는 제외)\n\n" + 
					"- 고객님의 사용 또는 일부 소비로 상품의 가치가 감소한 경우\n\n" +
					"- 시간이 지나 다시 판매하기 곤란할 정도로 상품의가치가 감소한 경우\n\n" +
					"- 복제가 가능한 상품의 포장이 훼손된 경우\n\n" +
					"- 고객님의 주문에 따라 개별적으로 생산되는 상품의 제작이 이미 진행된 경우\n\n",
					"취소/교환/환불"));


			faqs.add(new FAQEntity("교환(반품) 어떻게 진행 되나요?",
					"# 교환(반품) 진행 방법\n\n" +
					"- 받으신 상품을 교환(반품) 하실 경우, 교환 사유와 문제가 발생한 부분을 확인할 수 있는 사진과\n" +
					"함께 고객센터로 문의 바랍니다.\n\n" +
					"- 상담사를 통한 상담 이후, 교환(반품) 절차가 진행 됩니다.\n\n",
					"취소/교환/환불"));

			faqs.add(new FAQEntity("상품불량인 경우, 교환(반품) 기준이 궁금합니다.",
					"상품에 문제가 있는 경우\n\n" +
					"- 받으신 상품이 표시·광고 내용 또는 계약 내용과 다른 경우에는 상품을 받은 날부터 2일 이내에\n" +
					"교환 및 환불을 요청하실 수 있습니다.\n\n" +
					"- 상품의 정확한 상태를 확인할 수 있도록 사진을 함께\n" +
					"보내주시면 더 빠른 상담이 가능합니다.\n\n\n" +
					"※ 상품에 문제가 있는 것으로 확인되면 배송비는 GreenBox가 부담합니다.\n\n",
					"취소/교환/환불"));

			faqRepository.saveAll(faqs);

		} else {
			log.info("FAQ 데이터 있음");
		}
	}
}