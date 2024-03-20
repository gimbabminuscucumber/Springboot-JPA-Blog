let index = {
    init: function() {
        $("#btn-save").on("click", () => {			// function(){} 이 아닌 () => {} 을 사용한 이유는 this를 바인딩 하기 위해서
            this.save();
        });

        $("#btn-login").on("click", () => {
            this.login();
        });
    },

    save: function() {
        let data ={
		username: $("#username").val(),	// id가 username인 데이터가 가진 value를 username에 넣겠다
		password: $("#password").val(),
		email: $("#email").val()
	};
	
	console.log(data);
	
	// ajax 호출시, default가 비동기 호출
	// ajax 통신을 이용해 3개의 데이터를 JSON으로 변경하여 insert 요청 (username, password, email)
	// ajax가 통신을 성공하고 서버가 JSON을 리턴해주면 자동으로 자바 오브젝트로 변환해줌 (dataType: "json" >> 생략가능)
	$.ajax({							// 1. 회원가입 수행 요청
		type: "POST",
		url: "/blog/api/user",
		data: JSON.stringify(data),						// data : http body 데이터 / JSON.stringfy() : JS값이나, 객체를 JSON의 문자열로 변환
		contentType: "application/json; charset=utf-8",	// body데이터가 어떤 타입인지 (MIME)
		dataType: "json"								// 요청을 서버로해서 응답이 온 데이터는 기본적으로 모든 것은 문자열 (but 생긴게 JSON이라면 JS 오브젝트로 변경해서 보내줌)
	}).done(function(resp){			// 2. 성공시, function 실행
		alert("회원가입이 완료되었습니다.");
		alert("resp : " + resp);
		location.href="/blog";
	}).fail(function(error){				// 3. 실패시, function 실행
		alert(JSON.stringify(error));
	});	 
    },
    
     login: function() {
        let data ={
		username: $("#username").val(),
		password: $("#password").val()
	};
	
	$.ajax({						
		type: "POST",					// GET 방식은 url에 유저 정보가 찍혀서 POST 방식을 사용
		url: "/blog/api/user/login",
		data: JSON.stringify(data),						
		contentType: "application/json; charset=utf-8",	
		dataType: "json"				
	}).done(function(resp){			
		alert("로그인이 완료되었습니다.");
		location.href="/blog";
	}).fail(function(error){				
		alert(JSON.stringify(error));
	});	 
    }
};

index.init();