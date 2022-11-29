package kr.objet.okrproject.interfaces.user;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.domain.user.ProviderType;
import kr.objet.okrproject.domain.user.RoleType;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.jobtype.JobFieldDetail;
import lombok.Getter;

public class UserDto {

	@Getter
	public static class LoginResponse{

		@Schema(description = "신규 사용자 임시 id", example = "1")
		private String tempUserId;
		@Schema(description = "사용자 email", example = "okr@okr.com")
		private String email;

		@Schema(description = "사용자 이름", example = "홍길동")
		private String name;

		@Schema(description = "소셜 타입", example = "GOOGLE")
		private ProviderType providerType;

		@Schema(description = "권한", example = "USER")
		private RoleType roleType;

		@Schema(description = "직업", example = "GAME_PLANNER")
		private JobFieldDetail jobFieldDetail;

		@Schema(description = "프로필 이미지 링크", example = "프로필 이미지 링크")
		private String profileImage;

		@Schema(description = "AccessToken", example = "AccessToken")
		private String accessToken;

		@Schema(description = "RefreshToken", example = "RefreshToken")
		private String refreshToken;

		public LoginResponse(User user, String accessToken, String refreshToken) {
			this.email = user.getEmail();
			this.name = user.getUsername();
			this.jobFieldDetail = user.getJobField();
			this.profileImage = user.getProfileImageUrl();
			this.providerType = user.getProviderType();
			this.roleType = user.getRoleType();
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}
	}
}
