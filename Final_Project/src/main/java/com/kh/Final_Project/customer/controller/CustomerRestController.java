package com.kh.Final_Project.customer.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kh.Final_Project.customer.entity.Customer;
import com.kh.Final_Project.customer.service.CustomerServiceImp;
import com.kh.Final_Project.customer.vo.CustomerForm;
import com.kh.Final_Project.customerorder.service.CustomerOrderServiceImp;
import com.kh.Final_Project.product.entity.Product;
import com.kh.Final_Project.quitReason.service.QuitReasonServiceImp;
import com.kh.Final_Project.util.IdCheckRequest;
import com.kh.Final_Project.util.Util;
import com.kh.Final_Project.wishlist.service.WishListServiceImp;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/customer")
public class CustomerRestController {

	@Autowired
	private CustomerServiceImp service;

	@Autowired
	private QuitReasonServiceImp quitReasonService;
	
	@Autowired
	private WishListServiceImp wishListService;

	@Autowired
	private CustomerOrderServiceImp customerOrderServiceImp;

	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@RequestBody CustomerForm customerForm) {
		try {

			Customer saved = service.signUp(customerForm);

			if (saved != null) {
				return ResponseEntity.ok().body(Map.of("success", true, "message", "회원가입 성공!"));
			} else {
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "회원가입 실패"));
			}
			// 성공적으로 회원가입 처리가 완료되었다면, 성공 메시지와 함께 200 OK 응답을 반환합니다.
		} catch (Exception e) {
			// 예외가 발생한 경우, 실패 메시지와 함께 응답을 반환합니다.
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "회원가입 실패: " + e.getMessage()));
		}
	}

	@PostMapping("/idDuplicateCheck")
	public ResponseEntity<?> idDuplicateCheck(@RequestBody IdCheckRequest request) {
		boolean isIdAvailable = service.idDuplicateCheck(request.getId());

		System.out.println("과연 ? " + isIdAvailable);

		if (!isIdAvailable) {
			return ResponseEntity.ok().body("사용 가능한 아이디입니다.");
		} else {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 사용 중인 아이디입니다.");
		}
	}

	@PostMapping("/pwCheck")
	public ResponseEntity<?> pwCheck(@RequestBody Map<String, String> payload, HttpSession session) {

		String inputPassword = payload.get("password");
		Customer user = (Customer) session.getAttribute("loggedInCustomer");

		// oauth로 회원가입시 pw에 'oauthAPI로그인' 문자열이 들어간다 이때는 pw체크 안함
		if (user.getCustomerPw().contains("oauthAPI로그인")) {
			return ResponseEntity.ok().build();
		}

		boolean check = service.checkPw(user, inputPassword);

		if (check)
			return ResponseEntity.ok().build();

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}

	@PostMapping("/myInfoRe")
	public ResponseEntity<?> myInfoRe(@RequestBody CustomerForm customer, HttpSession session) {

		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");

		boolean success = service.myInfoRe(loggedInCustomer, customer);

		if (success) {
			return ResponseEntity.status(HttpStatus.OK).build();
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
		}

	}

	@PostMapping("/quitReason")
	public ResponseEntity<?> quitReason(@RequestBody Map<String, String> reasonMap, HttpSession session) {
		Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
		// 서비스 메서드 호출, 로직 처리는 서비스에서 수행
		quitReasonService.insert(reasonMap, loggedInCustomer,session);

		return ResponseEntity.status(HttpStatus.OK).build();
	}

	@PostMapping("/changePw")
	public ResponseEntity<?> changePw(@RequestBody Map<String, String> map) {

		int result = service.changePw(map);
		
		if(result == 3) {
			return ResponseEntity.ok().body(Map.of("success", true, "message", "비밀번호가 변경되었습니다."));
		}else if(result == 2) {
			return ResponseEntity.ok().body(Map.of("success", false, "message", "현재 비밀번호와 동일합니다."));
		}else if(result == 1) {
			return ResponseEntity.ok().body(Map.of("success", false, "message", "oauth 이용시 비밀번호 변경이 불가능합니다."));
		}else if(result == 0) {
			 return ResponseEntity.ok().body(Map.of("success",false,"message","비밀번호 변경에 실패하였습니다."));
		}

		return ResponseEntity.ok().body(Map.of("success",false,"message","비밀번호 변경에 실패하였습니다."));
	}
	
 // 찜하기 기능 처리하는 엔드포인트
    @PostMapping("/favorite/add")
    public ResponseEntity<?> addToFavorite(@RequestBody Map<String,String> id,
    		HttpServletRequest servletRequest) {
    	
    	System.out.println("id : " + id.get("productId"));
          	
        try {
            // 로그인 상태 확인
            HttpSession session = servletRequest.getSession(false);
            if (session != null && (Customer) session.getAttribute("loggedInCustomer") != null) {
            	Customer loggedInCustomer = (Customer) session.getAttribute("loggedInCustomer");
                // 로그인된 사용자 정보 가져오기
                String productIdStr = id.get("productId");
            	int result = wishListService.addToWishList(productIdStr, loggedInCustomer);
            	
            	if(result == 0) {
            		return ResponseEntity.ok().body(Map.of("success",true,"message","해당 상품이 존재하지 않습니다."));
            	}else if(result == 1){
            		return ResponseEntity.ok().body(Map.of("success",true,"message","찜목록에서 제거되었습니다."));
            	}else if(result == 2) {
            		return ResponseEntity.ok().body(Map.of("success",true,"message","찜목록에 추가되었습니다."));
            	}
            	
            	return  ResponseEntity.ok().body(Map.of("success",true,"message","오류 발생"));
                
            } else {
                return ResponseEntity.ok().body(Map.of("success",false,"message","로그인 후 이용 가능한 서비스 입니다."));
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }
    }
	
    @PostMapping("/cancel")
    public ResponseEntity<?> cancelProduct(@RequestBody String orderId){
    	
        if(!customerOrderServiceImp.isStart(orderId)) {
            // 주문 상태가 "배송 대기중"이 아니면 취소를 허용하지 않음
            return ResponseEntity.badRequest().build();
        }
        
        // 주문 취소 로직 수행
        customerOrderServiceImp.updateCancel(orderId);
        
        // 취소가 성공적으로 처리되었음을 클라이언트에게 알림
        return ResponseEntity.ok().build();
    }

}