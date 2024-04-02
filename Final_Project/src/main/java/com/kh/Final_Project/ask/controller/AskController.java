package com.kh.Final_Project.ask.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.kh.Final_Project.ask.entity.Ask;
import com.kh.Final_Project.ask.service.AskServiceImp;
import com.kh.Final_Project.ask.vo.AskForm;
import com.kh.Final_Project.askAnswer.entity.AskAnswer;
import com.kh.Final_Project.askAnswer.service.AskAnswerServiceImp;
import com.kh.Final_Project.customer.entity.Customer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ask")
public class AskController {
	
	@Autowired
	private AskServiceImp askServiceImp;

	@Autowired
	private AskAnswerServiceImp askAnswerImp;
	
	@GetMapping("/ask")
	public String ask(HttpServletRequest request, Model model) {
		
		// 로그인 상태일 때, 고객센터 페이지 보이게 하기
		HttpSession session = request.getSession(false); // 기존 세션 가져오기, 없으면 null 반환
		
	    if (session == null || session.getAttribute("loggedInCustomer") == null) {
	    	
	        // 로그인이 되어있지 않은 경우, 로그인이 필요하다는 알림창을 띄우고 로그인페이지로 이동시키기
	    	model.addAttribute("message", "1:1문의로 이동합니다. 로그인 하셔야 본 서비스를 이용할 수 있습니다.");
	    	model.addAttribute("searchUrl", "/customer/login");
	    	
	    	return "customerService/ask/msgAsk";
	    }
	    
	    // 세션에 저장된 회원들의 ocid 가져오기
	    Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
	    Long memberOcid = loggedInCustomer.getOcid();
	    String member = loggedInCustomer.getCustomerId();
	    
	    model.addAttribute("loggedInCustomer", loggedInCustomer);
	    model.addAttribute("memberId", memberOcid); // 회원ocid
	    model.addAttribute("member", member); // 회원id
	    
		return "customerService/ask/ask";
	}

	
	@GetMapping("/fragmentAsk/{fragmentName}")
    public String getFragmentAsk(@PathVariable String fragmentName,
    		Model model,
    		@RequestParam(defaultValue="0") int page,
    		HttpServletRequest request) {
    	
        String fragmentAsk = "customerService/ask/" + fragmentName;
        System.out.println("리턴값 : "+fragmentAsk);
        
        
        /* 페이징처리와 목록조회 */
        HttpSession session = request.getSession(false);
        Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

        Long memberOcid = loggedInCustomer.getOcid(); // 고객의 고유번호를 가져오기
        String member = loggedInCustomer.getCustomerId(); // 이이디 가져오기
        
        Page<Ask> list;
        
        if("admin".equals(member)) {// 아이디가 admin이면 문의 전체목록
        	list = askServiceImp.askListAll(page);
        }else { // 고객은 본인이 쓴 글만 볼 수 있음, 고객의 고유번호를 조회해서 쓴 글만 가져오기
        	list = askServiceImp.askListByMemberOcid(memberOcid, page);
        }
        
        // 답변 출력, Ask DB에서 ocid 가져오기(문의글의 고유번호)
        List<Long> ocidList = list.map(Ask::getOcid).getContent();
       
        if (!ocidList.isEmpty()) {
        	for (Long ocid : ocidList) {
                List<AskAnswer> answer = askAnswerImp.askAnswerList(ocid);
                String answerContent = null;
                
                // 답변이 있는 경우
                if (!answer.isEmpty()) {
                	int lastIndex = answer.size() - 1;
                    answerContent = answer.get(lastIndex).getContent();

                    // 답변이 있다면, 답변상태를 답변대기중에서 답변완료로 변경
                    askServiceImp.updateAnswerState(ocid, "답변완료");
                                
                } else {
                    // 답변이 없는 경우
                    answerContent = "답변 대기중입니다";
                }
                System.out.println(answer.size());
                
                // 모델에 추가
                model.addAttribute("answer", answerContent);
        	
        	}
        }
       

		model.addAttribute("lists",list.getContent());
		model.addAttribute("page", list);
		model.addAttribute("member", member);
		model.addAttribute("loggedInCustomer", loggedInCustomer);
		
        return fragmentAsk;
    }
	
	// 1:1문의 작성
	@PostMapping("/askWrite")
	public String askWrite(HttpServletRequest request,
			Model model,
			@RequestParam("file") MultipartFile file,
			AskForm askFrom) throws Exception{
		
		HttpSession session = request.getSession(false); // 기존 세션 가져오기, 없으면 null 반환
	    Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
	    Long memberOcid = loggedInCustomer.getOcid(); // 회원의 ocid
	    String realId = loggedInCustomer.getCustomerId();
	    model.addAttribute("loggedInCustomer", loggedInCustomer);

		askServiceImp.write(askFrom, file, memberOcid, realId);
		model.addAttribute("message", "작성이 완료되었습니다.");
		model.addAttribute("searchUrl", "ask");
		
		return "customerService/ask/msgAsk";
	}


	// 답변추가
	@PostMapping("/ask")
	public String askAnswer(
			@RequestParam Long askBoardNO,
			@RequestParam String content,
			Model model) {
		askAnswerImp.answerWrite(askBoardNO, content);
		
		model.addAttribute("message", "작성이 완료되었습니다.");
		model.addAttribute("searchUrl", "ask");
		
		return "customerService/ask/msgAsk";
	}
	
	// 0312 게시글과 답변을 모두삭제(관리자만 가능)
	@PostMapping("/delete/{ocid}")
	public String deleteAll(@PathVariable Long ocid, Model model) {
		
		askServiceImp.deleteAll(ocid);

	    return "redirect:/ask/ask";
	}
	
	
}




