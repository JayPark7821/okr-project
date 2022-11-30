package kr.objet.okrproject.domain.user;


import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.guest.GuestInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import lombok.Getter;

public class UserInfo {

	@Getter
	public static class Main {
		private final String id;
		private final String email;
		private final String name;
		private final ProviderType providerType;
		private final RoleType roleType;
		private final JobFieldDetail jobFieldDetail;
		private final String profileImage;

		public Main(UserInfo.Main main) {
			this.id = main.getId();
			this.email = main.getEmail();
			this.name = main.getName();
			this.providerType = main.getProviderType();
			this.roleType = main.getRoleType();
			this.jobFieldDetail = main.getJobFieldDetail();
			this.profileImage = main.getProfileImage();
		}

		public Main(User user) {
			this.id = user.getUserId();
			this.email = user.getEmail();
			this.name = user.getUsername();
			this.providerType = user.getProviderType();
			this.roleType = user.getRoleType();
			this.jobFieldDetail = user.getJobField();
			this.profileImage = user.getProfileImageUrl();
		}
	}

	@Getter
	public static class Response {

		private static final String GUEST_PREFIX = "guest_";
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

		public static Response login(UserInfo.Main userInfo, String accessToken, String refreshToken) {
			return new Response(
				userInfo.getId(),
				userInfo.getEmail(),
				userInfo.getName(),
				userInfo.getProviderType(),
				userInfo.getRoleType(),
				userInfo.getJobFieldDetail(),
				userInfo.getProfileImage(),
				null,
				accessToken,
				refreshToken
			);
		}

		public static Response join(GuestInfo.Main guestInfo) {
			return new Response(
				guestInfo.getGuestId(),
				guestInfo.getEmail(),
				guestInfo.getGuestName(),
				guestInfo.getProviderType(),
				null,
				null,
				guestInfo.getProfileImageUrl(),
				TokenGenerator.randomCharacterWithPrefix(GUEST_PREFIX),
				null,
				null
			);
		}
	}


	@Getter
	public static class UserEntity{
		private final Long userSeq;
		private final String userId;
		private final String username;
		private final String password;
		private final String email;
		private final String emailVerifiedYn;
		private final String profileImageUrl;
		private final ProviderType providerType;
		private final RoleType roleType;
		private final JobFieldDetail jobField;

		public UserEntity(User user) {
			this.userSeq = user.getUserSeq();
			this.userId = user.getUserId();
			this.username = user.getUsername();
			this.password = user.getPassword();
			this.email = user.getEmail();
			this.emailVerifiedYn = user.getEmailVerifiedYn();
			this.profileImageUrl = user.getProfileImageUrl();
			this.providerType = user.getProviderType();
			this.roleType = user.getRoleType();
			this.jobField = user.getJobField();
		}

		public User toEntity() {
			return new User(
				this.userSeq,
				this.userId,
				this.username,
				this.password,
				this.email,
				this.emailVerifiedYn,
				this.profileImageUrl,
				this.providerType,
				this.roleType,
				this.jobField
			);
		}
	}

}
