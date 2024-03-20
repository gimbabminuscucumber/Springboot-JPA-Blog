package com.cos.blog.dto;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDto<T> {
	int status;			// int 로 바꾼 후, UserApiController와 GlobalExceptionHandler 클래스에서
	T data;				// - 'new ResponseDto<Integer>(HttpStatus.OK.value()' 뒤에 .value() 를 붙이면,  
}								// - insert 시, 정상이면 200 / 에러면 500 이라는 '숫자'가 들어감 (.value() 안붙이면 문자열이 들어옴)