package kr.objet.okrproject.domain.guest;

import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.application.user.UserInfo;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import lombok.Builder;
import lombok.Getter;

public class GuestCommand {
	private static final String GUEST_PREFIX = "guest_";

	@Getter
	public static class Join {

		private final String guestUuId;

		private final String name;

		private final String email;

		private final JobFieldDetail jobField;

		@Builder
		public Join(String guestUuId, String name, String email, String jobField) {
			this.guestUuId = guestUuId;
			this.name = name;
			this.email = email;
			this.jobField = JobFieldDetail.lookup(jobField);
		}

		public User toUserEntity(Guest guest, String initialPw) {
			return User.builder()
				.userId(guest.getGuestId())
				.email(guest.getEmail())
				.roleType(RoleType.USER)
				.password(initialPw)
				.username(this.name)
				.emailVerifiedYn("Y")
				.jobField(this.jobField)
				.profileImageUrl(guest.getProfileImageUrl())
				.providerType(guest.getProviderType())
				.build();
		}

	}

	@Getter
	public static class RegisterGuest {
		private final String uuid;
		private final String id;
		private final String name;
		private final String email;
		private final String imageUrl;
		private final ProviderType providerType;

		@Builder
		public RegisterGuest(OAuth2UserInfo user, ProviderType providerType) {
			this.uuid = TokenGenerator.randomCharacterWithPrefix(GUEST_PREFIX);
			this.id = user.getId();
			this.name = user.getName();
			this.email = user.getEmail();
			this.imageUrl = user.getImageUrl();
			this.providerType = providerType;
		}

		public Guest toEntity() {
			return Guest.builder()
				.uuid(this.uuid)
				.guestId(this.id)
				.guestName(this.name)
				.email(this.email)
				.providerType(this.providerType)
				.profileImageUrl(this.imageUrl)
				.build();
		}
	}

}
