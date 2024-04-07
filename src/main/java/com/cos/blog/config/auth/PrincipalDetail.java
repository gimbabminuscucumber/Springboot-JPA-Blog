package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.blog.model.User;

import lombok.Data;
import lombok.Getter;

// 시큐리티가 로그인 요청을 가로채서 로그인 진행 후 완료가 되면 
// - UserDetails 타입의 오브젝트를 시큐리티 세션에 저장을 해준다
@SuppressWarnings("serial")
@Getter
public class PrincipalDetail implements UserDetails{	// implements가 private User user 의 타입을 UserDetails로 변경시킴

	private User user;			// 콤포지션 : 객체를 품고 있는 것
											// - PrincipalDetail 클래스를 호출하면 DI를 통해 User 객체 사용가능
	
	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();		// PrincipalDetail 클래스를 호출하면 DI를 통해 User 객체의 변수인 password 사용가능
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료되지 않았는지 리턴 (true : 만료 안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨있는지 아닌지를 리턴 (true : 안 잠김)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료되지 않았는지 리턴 (true : 만료 안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	// 계정이 활성화(사용가능)인지 리턴 (true : 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 계정이 갖고있는 권한 목록을 리턴한다. (권한이 여러 개 있을 수 있어서 루프를 돌아야 하는데 우리는 한개만)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		
		// 방법 1.
//		collectors.add(new GrantedAuthority() {		// 자바는 매개변수로 메소드를 넣을 수 없음, 오브젝트는 가능
//			
//			@Override
//			public String getAuthority() {
//				return "ROLE_" + user.getRole();	// "ROLE_"는 스프링에서 ROLE을 사용할 때 규칙 ex) ROLE_USER
//			}
//		});
		
		// 방법 2.
		collectors.add(() -> { return "ROLE_" + user.getRole();});
		
		return collectors;
	}
	
}
