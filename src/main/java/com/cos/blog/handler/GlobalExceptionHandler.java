package com.cos.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;


@ControllerAdvice		// 어느 패키지, 어느 클래스에서 에러가 발생하든 이 클래스로 들어옴
@RestController
public class GlobalExceptionHandler {

	@ExceptionHandler(value = Exception.class)			// Exception 이 발생하면 해당 메소드 실행 (Exception 자리에 특정 예외를 넣으면 해당 예외만 처리함)
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());	// 500 에러
	}
}