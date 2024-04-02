package com.kh.Final_Project.ask.service;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.kh.Final_Project.ask.entity.Ask;
import com.kh.Final_Project.ask.repository.AskRepository;
import com.kh.Final_Project.ask.vo.AskForm;
import com.kh.Final_Project.askAnswer.repository.AskAnswerRepository;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class AskServiceImp implements AskService{
	
	@Autowired
	private AskRepository askRepository;
	
	@Autowired
	private AskAnswerRepository askAnswerRepository;
	
	/* 파일 저장 */
	public String saveImg(
			MultipartFile reloadImg, String savePath) {
		
		File folder = new File(savePath);
		
		// 폴더가 없을 때 생성한다.
		if(folder.exists()== false) {
			folder.mkdirs();
		}
		
		// 파일 이름 랜덤으로 바꾸기
		String originalImgName = reloadImg.getOriginalFilename();
		String reImgName = LocalDateTime.now().
							format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		reImgName += originalImgName.substring(originalImgName.lastIndexOf("."));
		String reImgPath =  savePath + "/" + reImgName;
		
		try {
			
			reloadImg.transferTo(new File(reImgPath));
			
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return reImgName;
	}
	
	/* 게시글 저장 */
	public void write(AskForm askForm, MultipartFile file, Long memberId, String realId) throws Exception {
		
		askForm.setMemberId(memberId);
		askForm.setCustmerRealId(realId);
		
		// 최초 문의 작성시 기본적으로 답변상태가 답변대기중으로 저장됨
		askForm.setAnswerState("답변대기중"); 
		
		if(file != null && file.isEmpty() == false) {
			// 실제파일이 저장되는 경로
			String projectPath = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\files";
			
			String renameImgName = saveImg(file, projectPath);
			if(renameImgName != null) {
				askForm.setOriginalFile(file.getOriginalFilename());
				askForm.setRenamedFile(renameImgName);
				askForm.setImageFilePath("/files/" + renameImgName);
			}
			
		}
		Ask ask = askForm.toEntity();
		askRepository.save(ask);
	}
	
	/* 페이지 처리 및 목록 (회원용)*/
	@Override
	public Page<Ask> askListByMemberOcid(Long memberOcid, int page) {
		
		// 1페이지 당 몇개씩 보여줄건지
		int pageSize = 1;
		// Pageable에 넘기기
		Pageable pageable = PageRequest.of(page,pageSize, Sort.by("writeDate").descending());
		// Page에서 게시글 목록 가져오기
		Page<Ask> list = askRepository.findByMemberId(memberOcid, pageable);
		
		return list;
	}
	
	/* 페이지 처리 및 목록(관리자용) */
	@Override
	public Page<Ask> askListAll(int page) {
		
		// 1페이지 당 몇개씩 보여줄건지
		int pageSize = 1;
		// Pageable에 넘기기
		Pageable pageable = PageRequest.of(page,pageSize, Sort.by("writeDate").descending());
		// Page에서 게시글 목록 가져오기
		Page<Ask> list = askRepository.findAll(pageable);
		
		return list;
	}
	
	/* 답변상태 변경하기 */
	@Override
	public void updateAnswerState(Long ocid, String answerState) {
		
		Ask askStateUpdate = askRepository.findById(ocid).orElse(null);
		
        if (askStateUpdate != null) {
        	askStateUpdate.setAnswerState(answerState);
            askRepository.save(askStateUpdate);
        }        
	}
	
	/* 0312 모두삭제 */
	@Transactional
	@Override
	public void deleteAll(Long ocid) {
		
		Ask askDeleteImg = askRepository.findById(ocid).orElse(null);
		if(askDeleteImg != null) {
			
			String imgPath = askDeleteImg.getImageFilePath();
			// 가져온 이미지 경로가 비어있는지 아닌지 확인
			if(imgPath!=null && !imgPath.isEmpty()) {
				
				String imgFullpath = System.getProperty("user.dir")+"\\src\\main\\resources\\static" + imgPath;
				
				File deleteImg = new File(imgFullpath);
				
				// 파일 유무 확인
				if(deleteImg.exists()) {
					boolean delete = deleteImg.delete();
					if(!delete) {
					}else {
					}
				}else { // 파일 유무 확인에 대한 if문 끝
				}
			} // 가져온 이미지 경로가 비어있는지 아닌지 확인에 대한 if문 끝
		}
		
		// 해당 문의글의 답변 먼저 삭제
		askAnswerRepository.deleteByBoardNo(ocid);
		// 문의글 삭제
		askRepository.deleteById(ocid);
	}
	
}