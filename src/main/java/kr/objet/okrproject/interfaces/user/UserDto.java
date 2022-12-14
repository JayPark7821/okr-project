package kr.objet.okrproject.interfaces.user;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserDto {

	@Getter
	@NoArgsConstructor
	public static class RegisterRequest {
		@Valid
		@NotNull(message = "임시 유저ID는 필수 값입니다.")
		@Schema(description = "임시 번호", example = "1")
		private String guestUuid;

		@Valid
		@NotNull(message = "사용자 이름은 필수 값입니다.")
		@Size(min = 1, max = 100)
		@Schema(description = "사용자 이름", example = "홍길동")
		private String name;

		@Valid
		@NotNull(message = "사용자 email은 필수 값입니다.")
		@Schema(description = "사용자 email", example = "okr@okr.com")
		@Size(max = 512)
		@Email
		private String email;

		@Size(min = 1, max = 100)
		@Schema(description = "대표 분야", example = "백엔드 개발자")
		private String jobField;

		public GuestCommand.Join toCommand() {
			return GuestCommand.Join.builder()
				.guestUuId(this.guestUuid)
				.name(this.name)
				.email(this.email)
				.jobField(this.jobField)
				.build();
		}

	}

	@Getter
	public static class LoginResponse {

		@Schema(description = "신규 사용자 임시 id", example = "1")
		private String guestUserId;
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

		public LoginResponse(UserInfo.Response response) {
			this.guestUserId = response.getGuestUuid();
			this.email = response.getEmail();
			this.name = response.getName();
			this.jobFieldDetail = response.getJobFieldDetail();
			this.profileImage = response.getProfileImage();
			this.providerType = response.getProviderType();
			this.roleType = response.getRoleType();
			this.accessToken = response.getAccessToken();
			this.refreshToken = response.getRefreshToken();
		}
	}

	@Getter
	@NoArgsConstructor
	public static class UserBrief {

		@Schema(description = "유저명", example = "홍길동")
		private String userName;

		@Schema(description = "이미지url", example = "~~~~~")
		private String profileImageUrl;

		@Schema(description = "대표분야", example = "백엔드 개발자")
		private String jobField;

		public UserBrief(User user) {
			this.userName = user.getUsername();
			this.profileImageUrl = user.getProfileImageUrl();
			this.jobField = user.getJobField().getTitle();
		}
	}

}
