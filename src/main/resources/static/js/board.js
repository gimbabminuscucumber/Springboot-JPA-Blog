let index = {
	init: function() {
		$("#btn-save").on("click", () => {		
			this.save();
		});
	    
		$("#btn-delete").on("click", () => {		
			this.deleteById();		// delete가 약어라서 변수명을 deleteById로 설정
		});
	
		$("#btn-update").on("click", () => {		
			this.update();
		});
	
		$("#btn-reply-save").on("click", () => {		
			this.replySave();
		});
	},

	save: function() {
		let data ={
			title: $("#title").val(),			
			content: $("#content").val()
		};

		$.ajax({					
			type: "POST",
			url: "/api/board",
			data: JSON.stringify(data),						
			contentType: "application/json; charset=utf-8",	
			dataType: "json"							
		}).done(function(resp){			
			alert("글쓰기가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){				
			alert(JSON.stringify(error));
		});	 
	},

	deleteById: function() {
		let id = $("#id").text();
	
		console.log("삭제할 게시글 id : " + id);
	
		$.ajax({					
			type: "DELETE",
			url: "/api/board/" + id,
			dataType: "json"							
		}).done(function(resp){			
			alert("삭제가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){				
			alert(JSON.stringify(error));
		});	 
	},
   
   update: function() {
		let id = $("#id").val();
	   
		let data ={
			title: $("#title").val(),			
			content: $("#content").val()
		};
	
		$.ajax({					
			type: "PUT",
			url: "/api/board/" + id,			// 백틱 : `/api/board/"${id}`
			data: JSON.stringify(data),						
			contentType: "application/json; charset=utf-8",	
			dataType: "json"							
		}).done(function(resp){			
			alert("수정이 완료되었습니다.");
			location.href="/";
		}).fail(function(error){				
			alert(JSON.stringify(error));
		});	 
	},
	
	replySave: function() {
		let data ={
			userId: $("#userId").val(),
			boardId: $("#boardId").val(),
			content: $("#reply-content").val()
		};
		
		$.ajax({					
			type: "POST",
			url: `/api/board/${data.boardId}/reply`,	// JS의 변수값(${data.boardId})이 문자열로 들어옴 / 백틱?? ``
			data: JSON.stringify(data),						
			contentType: "application/json; charset=utf-8",	
			dataType: "json"							
		}).done(function(resp){			
			alert("댓글 작성이 완료되었습니다.");
			location.href=`/board/${data.boardId}`;
		}).fail(function(error){				
			alert(JSON.stringify(error));
		});
	},

	replyDelete : function(boardId, replyId){
		$.ajax({ 
			type: "DELETE",
			url: `/api/board/${boardId}/reply/${replyId}`,
			dataType: "json"
		}).done(function(resp){
			alert("댓글을 삭제했습니다.");
			location.href = `/board/${boardId}`;
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); 
	},
	
}

index.init();