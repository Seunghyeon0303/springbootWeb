/**
 * 파일명: main.js
 */

$(document).ready(function() {
	console.log("찍히니?");

	// 제목을 클릭하면 이벤트 발생! click이벤트 추가!

	$('.showDetail').click(function(event) {
		event.preventDefault(); // 링크의 기본 동작을 막는다.
//		console.log("클릭했다!");
		// 우리는 상세페이지를 모달에 띄우기 위해서 필요한 것! 
		// 실제 글의 번호를 가지고 와서 데이터베이스에
		// 글의 번호를 이용해서 DB정보를 하나만 가져와야된다.

		// 1. 클릭된 요소에서 부모요소를 찾는다.
		// 해당되는 글의 번호를 가지고 온다.
		// closest('태그')
		// 현재 나를 기준으로 가장 가까운 부모 태그를 찾는다.
		var boardNo = $(this).closest('tr').find('td:first-child').text().trim();

		// 대부분 요소값에서 데이터를 꺼내올 때 특별한 일이 없으면 문자형태로 가져온다.
		// 우리가 저장했던 DB에 Long id값과 타입이 맞지 않아서 타입 에러가 난다.!

		// 문자 -> 숫자형태로 변경
		boardNo = parseInt(boardNo);
//		console.log(boardNo);
//		console.log(typeof boardNo);

		// AJAX 요청을 통해서 상세 페이지의 내용을 가져온다.
		// 헤더와 바디 정보를 객체 형식으로 보낸다.
		// RestControll , Controller랑 url충돌 나지 않도록 조심해야 된다.
		$.ajax({
			url : '/board/detailModal/' + boardNo,
			type : 'GET',
			dataType : 'html',
			// 성공시 응답받는 객체 넘어온다.
			success : function(response) {

			var obj = JSON.parse(response);
			console.log(obj);

			// .html()
			// 매개변수는 문자열 형태의 html
			// 콘텐츠를 받는다.
			// 위에 가져온 객체를 html 테이블 형식으로 문자로 연결해서 만들어 주면 된다.

			var htmlContent = '<table class="table table-boardered">';
			htmlContent += '<tr><th>글번호</th><td>' + obj.no + '</td></tr>';
			htmlContent += '<tr><th>제목</th><td>' + obj.title + '</td></tr>';
			htmlContent += '<tr><th>작성자번호</th><td>' + obj.writerNo + '</td></tr>';
			htmlContent += '<tr><th>내용</th><td>' + obj.content + '</td></tr>';
			htmlContent += '<tr><th>조회수</th><td>' + obj.readCount + '</td></tr>';
			htmlContent += '<tr><th>작성 시간</th><td>' + obj.createDate + '</td></tr>';

			// 이하 필요한 데이터를 추가로 표시하도록 수정한다.
			htmlContent += '</table>';

			// 모달의 요소값을 가져온다.
			$('#modalContent').html(htmlContent);
			// 모달을 연다.
			$('#myModal').modal('show');
				},
				error : function(xhr, status, error) {
				// 에러가 발생한 경우 실행되는 함수
				console.error(xhr.responseText);
				}
		}); // ajax 끝
	}); // 이벤트 끝

	// 게시글검색 버튼을 클릭했을 때 실행하는 이벤트
	$('#searchBtn').click(function() {
		console.log("게시글 검색 버튼 클릭!");
		event.preventDefault(); // 링크의 기본 동작을 막는다.
		// 전체 게시글을 가져오는 ajax만 보내면 되기 때문에!
		$.ajax({
			url : '/board/all',
			type : 'GET', // 전송방식 get
			dataType : 'json', // json타입으로 설정한다.
			// 성공시 실행할 함수
			success : function(response) {
				console.log(response);
				console.log(typeof response);

			var htmlContent = '<table class="table table-bordered">';
			htmlContent += '<thead>';
			htmlContent += '<tr>';
			htmlContent += '<th scope="col">글번호</th>';
			htmlContent += '<th scope="col">제목</th>';
			htmlContent += '<th scope="col">작성자 번호</th>';
			htmlContent += '<th scope="col">내용</th>';
			htmlContent += '<th scope="col">조회수</th>';
			htmlContent += '<th scope="col">작성 시간</th>';
			htmlContent += '</tr>';
			htmlContent += '</thead>';
			htmlContent += '<tbody>';

			// response가 배열인 경우에는 대비하여
			// forEach()를 이용해서 각 객체를 처리한다.
			response.forEach(function(data) {
				htmlContent += '<tr>';
				htmlContent += '<td>' + data.no + '</td>';
				htmlContent += '<td>' + data.title + '</td>';
				htmlContent += '<td>' + data.writerNo + '</td>';
				htmlContent += '<td>' + data.content + '</td>';
				htmlContent += '<td>' + data.readCount + '</td>';
				htmlContent += '<td>' + data.createDate + '</td>';
				htmlContent += '</tr>';
				});

				htmlContent += '</tbody>';
				htmlContent += '</table>';

				// 모달의 요소값을 가져온다.
				$('#modalContent').html(htmlContent);
				// 모달을 연다.
				$('#myModal').modal('show');
				},
				error : function(xhr, status, error) {
				// 에러가 발생한 경우 실행되는 함수
				console.error(xhr.responseText);
			}
		}); // ajax끝!
	}); // 이벤트 끝!

	// 삭제 버튼을 가져와서 클릭을 했을 때 실행하는 메서드
	$('#deleteBtn').click(function() {
		
		event.pereventDefault();
		
		// 요소값의 링크를 가져오기
		// href 속성을 가져오기!
//		var url = $(this).attr('href');
		console.log(url);
		
		$.ajax({
			url : 'board/delete',		  // 삭제를 요청보낼 url
			type : 'DELETE',  // http 요청 메서드
			success : function(data) {
				// 성공 시 콜백 함수
				alert(data);
				// 삭제 성공 시 추가 작업 상세 페이지가 없어졌기 때문에
				// 그냥 index 페이지로 이동
				window.location.href="/";
			},
			error : function(xhr, textStatus, errorThrow)
			// 실패 시 콜백함수
			alert("게시물 삭제에 실패했습니다!");
		});
	});
	
	// 모달창안에 검색창에 키보드로 작성후 엔터치면 이벤트 발생!
	$('#searchModelInput').keypress(function(event) {

	// 엔터 키를 눌렀을 때
	if (event.which === 13) {
		event.preventDefault();

		// 입력된 값 가져오기
		var input = $(this).val();
		console.log(input);

		// 검색어로 입력된 값을 가져왔다. 그러면 검색된 값이
		// 타이틀과 내용을 모두 검색해서 결과값을 돌려받는다.

		$.ajax({
			url : '/board/searchModal', // 검색어를 처리하는 url
			type : 'POST',
			data : {
			keyword : input }, // 검색어를 파라미터로 전달
			dataType : 'json',
			success : function(response) {
				// 검색한 결과를 테이블형식으로 구성하여 모달에 출력
				var htmlContent = '<table class="table table-bordered">';
				htmlContent += '<thead>';
				htmlContent += '<tr>';
				htmlContent += '<th scope="col">글번호</th>';
				htmlContent += '<th scope="col">제목</th>';
				htmlContent += '<th scope="col">작성자 번호</th>';
				htmlContent += '<th scope="col">내용</th>';
				htmlContent += '<th scope="col">조회수</th>';
				htmlContent += '<th scope="col">작성 시간</th>';
				htmlContent += '</tr>';
				htmlContent += '</thead>';
				htmlContent += '<tbody>';

				// 반복되는 내용들을 출력하는 바디를 만들어 준다.
				for (var i = 0; i < response.length; i++) {

					htmlContent += '<tr>';
					htmlContent += '<td>' + response[i].no + '</td>';
					htmlContent += '<td>' + response[i].title + '</td>';
					htmlContent += '<td>' + response[i].writerNo + '</td>';
					htmlContent += '<td>' + response[i].content + '</td>';
					htmlContent += '<td>' + response[i].readCount + '</td>';
					htmlContent += '<td>' + response[i].createDate + '</td>';
					htmlContent += '</tr>'
					}

					htmlContent += '</tbody>';
					htmlContent += '</table>';

					// 모달의 요소값을 가져온다.
					$('#modalContent').html(htmlContent);
					// 모달을 연다.
					$('#myModal').modal('show');
					},
					error : function(xhr, status, error) {
					console.error(xhr.responseText); // 에러 발생 시 콘솔에 로그 출력
				}

			}); // ajax끝
		}
	});
});// 제이쿼리 끝!
