/**
 * 파일명: main.js 
 */

$(document).ready(function() {
	console.log("잘 찍히니?");
	
	// input창에 값이 변경되면 input창의 값을 
	// 콘솔에 찍어주는 이벤트를 등록하겠다. 
	
	$('#inputField').on('input',function(){
		
		// 입력된 값을 가져오기 
		var inputValue = $(this).val();
		console.log(inputValue);
		
		// 입력된 값이 4자리 이상이면 가입가능
		// 만약 4자리 미만이면 가입 불가 (4자리 이상 입력해주세요)
		// 메시지를 출력하는 displayField 요소값 가져오기
		if(inputValue.length >= 4){
			console.log("가능?");
			$('#displayField').text('가입 가능');
			$('#displayField').css('color', 'green');
			
		}else{
			$('#displayField')
				.text('가입 불가 (4자리 이상 입력하세요!)');
			$('#displayField').css('color', 'red');
		}		
	});
	
	// 비밀번호 입력시 특수문자,대소문자,숫자 1개 이상
	$('#pswd1').on('input',function(){
		
		// 입력된 값을 가져오기 
		var inputValue = $(this).val();
		console.log(inputValue);
		
		// 입력된 비밀번호가 위에 내용을 포함하고 있는지 검사해야된다
		var passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)[A-Za-z\d@$!%*?&]{4,10}$/;
		
		if(passwordPattern.test(inputValue)){
			// 내가 원하는 조건을 모두 만족 했다면 
			$('#pswd1_img1').attr('src', '/images/m_icon_safe.png');
			$('#displayPw').text('가입 가능');
			$('#displayPw').css('color','green');
				
		}else {
			// 만약 원하는 조건을 만족하지 못한 경우 
			// $('#pswd1_img1').attr('src', '/images/m_icon_danger.png');
			
			// 만약 대소문자특수문자숫자 한개이상은 입력하세요!
			// 빨간색으로 띄우기!
			$('#pswd1_img1').attr('src', '/images/m_icon_not_use.png');
			$('#displayPw').text('대소문자,특수문자,숫자 하나 이상입력해주세요');
			$('#displayPw').css('color', 'red');
		}		
	});
	
	// 중복확인하는 이벤트를 작성하면 된다.
	$('#checkDuplicateButton').click(function() {
		// 1. 입력창에 있는 값 가져오기
		var userId = $('#inputField').val();
		console.log(userId);
		
		// 2. restController 호출하는 비동기 통신 fetch api를 이용
		fetch('/check-duplicate', {
			// 3. 전송방식
			method : 'POST', // 전송방식은 post로 요청한다.
			headers : { 	 // Content-Type 설정한다.
				'Content-Type': 'application/json'
			},
			// JSON 형태로 아이디를 전송
			body : JSON.stringify({userid : userId})
		})
		.then(response => response.json())	// 응답을 JSON 형태로 반환한다.
		.then(data => {
			// 성공적으로 응답을 받았을 때 실행되는 함수
			// 여기서 응답을 통해 중복 확인 결과를 displayField에 출력한다.
			console.log(data);
			$('#displayField').text(data.message);	// 응답메시지 출력
		})
		.catch(error => {
			// 요청이 실패했을 때 실행되는 함수
			// 중복 아이디가 있을 경우에 출력하기 위해서 예외처리한 것 뿐!
			$('#displayField').text('서버와의 통신 중 오류가 발생했습니다.');	// 응답메시지 출력
		}); 
	});
	
	// 비밀번호 재확인 입력란에서 값이 변경될 때 이벤트 발생 
	$('#pswd2').on('input',function(){
		
		// 입력된 값을 가져오기 
		var inputValue = $(this).val();
		console.log(inputValue);
		
				
		if(inputValue === $('#pswd1').val()){
			// 내가 원하는 조건을 모두 만족 했다면 
			$('#pswd1_img1').attr('src', '/images/m_icon_safe.png');		
			$('#displayPw2').text('가입 가능');
			$('#displayPw2').css('color','green');
			
		}else{
			
			$('#pswd2_img1').attr('src', '/images/m_icon_not_use.png');
			$('#displayPw2').text('비밀번호가 일치 하지 않습니다.');
			$('#displayPw2').css('color','red');
		}		
	});
	
	// 가입하기 버튼을 누르면 input 창에 있는 모든 내용을 가져오기
	$('#btnJoin').on('click', function() {
		console.log("가입하기 버튼 누름!");
		// 1. input창 모든 값 가져오기
		var userInfo = {
				email : $('#email').val(),
				password : $('#pswd1').val(),
				passwordConfirm : $('#pswd2').val(),
				name : $('#name').val(),
				birthYear : $('#yy').val(),
				birthMonth : $('#mm').val(),
				birthDay : $('#dd').val(),
				gender : $('#gender').val(),
				phoneNumber : $('#mobile').val()
		};
		console.log(userInfo);
		
		// 2. restController로 데이터를 전송하기
		// fetch('url')
		fetch('/signup', {
			method : 'POST',
			headers : {
				'Content-Type': 'application/json'
			},
			body : JSON.stringify(userInfo)
		})
		.then(response => response.text())
		.then(data => {
			showModal("정상적으로 가입 되었습니다.");
			// console.log("정상적으로 가입 되었습니다.");
			console.log(data);
		})
		.catch(error => {
			console.log("Error : ", error);
		});
	});
	
});	// 제이쿼리 끝나는 중괄호 !

// 모달 창을 닫는 함수
// 만약 close 버튼을 클릭했을 때 안닫히는 경우
function closeModal() {
	$('#myModal').modal('hide');
}

// 모달 창을 보여주는 함수
function showModal(message) {
	// 모달 창에 메시지 출력
	$('#modalMessage').text(message);
	// 모달 창 보이기
	$('#myModal').modal('show');
}