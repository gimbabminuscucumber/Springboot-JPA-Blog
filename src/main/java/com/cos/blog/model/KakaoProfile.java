package com.cos.blog.model;

import lombok.Data;

@Data
public class KakaoProfile {				// 외부 클래스
	public Long id;			// Long > Integer
	public String connected_at;
	public Properties properties;
	public KakaoAccount kakao_account;

	@Data
	public class Properties {				// 내부 클래스 1
		public String nickname;
		public String profile_image;
		public String thumbnail_image;
	}

	@Data
	public class KakaoAccount {			// 내부 클래스 2
		public Boolean profile_nickname_needs_agreement;
		public Boolean profile_image_needs_agreement;
		public Profile profile;
		public Boolean has_email;
		public Boolean email_needs_agreement; 
		public Boolean is_email_valid;
		public Boolean is_email_verified;
		public String email;

		@Data
		public class Profile {					// 내부 클래스 2의 내부 클래스
			public String nickname;
			public String thumbnail_image_url;
			public String profile_image_url;
			public Boolean is_default_image;
			public Boolean is_default_nickname;
		}
	}
}
  