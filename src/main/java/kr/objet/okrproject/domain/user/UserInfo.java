package kr.objet.okrproject.domain.user;


import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserInfo {

	@Getter
	public static class Response {

		private final String id;
		private final String email;
		private final String name;
		private final ProviderType providerType;
		private final RoleType roleType;
		private final JobFieldDetail jobFieldDetail;
		private final String profileImage;
		private final String guestUuid;
		private final String accessToken;
		private final String refreshToken;

		public Response(String id, String email, String name, ProviderType providerType, RoleType roleType,
			JobFieldDetail jobFieldDetail, String profileImage, String guestUuid, String accessToken,
			String refreshToken) {
			this.id = id;
			this.email = email;
			this.name = name;
			this.providerType = providerType;
			this.roleType = roleType;
			this.jobFieldDetail = jobFieldDetail;
			this.profileImage = profileImage;
			this.guestUuid = guestUuid;
			this.accessToken = accessToken;
			this.refreshToken = refreshToken;
		}

		public static Response login(User user, String accessToken, String refreshToken) {
			return new Response(
				user.getUserId(),
				user.getEmail(),
				user.getUsername(),
				user.getProviderType(),
				user.getRoleType(),
				user.getJobField(),
				user.getProfileImageUrl(),
				null,
				accessToken,
				refreshToken
			);
		}

		public static Response join(Guest guest) {
			return new Response(
				guest.getGuestId(),
				guest.getEmail(),
				guest.getGuestName(),
				guest.getProviderType(),
				null,
				null,
				guest.getProfileImageUrl(),
				guest.getGuestUuid(),
				null,
				null
			);
		}
	}

	@Getter
	@NoArgsConstructor
	public static class AuthProcess {
		private String id;
		private String email;
		private String name;
		private ProviderType providerType;
		private RoleType roleType;
		private JobFieldDetail jobFieldDetail;
		private String profileImage;
		private boolean isJoining;

		public AuthProcess(User user) {
			this.id = user.getUserId();
			this.email = user.getEmail();
			this.name = user.getUsername();
			this.providerType = user.getProviderType();
			this.roleType = user.getRoleType();
			this.jobFieldDetail = user.getJobField();
			this.profileImage = user.getProfileImageUrl();
			this.isJoining = false;
		}

		public AuthProcess(OAuth2UserInfo info, ProviderType provider) {
			this.id = info.getId();
			this.email = info.getEmail();
			this.name = info.getName();
			this.providerType = provider;
			this.profileImage = info.getImageUrl();
			this.isJoining = true;
		}
	}

}
