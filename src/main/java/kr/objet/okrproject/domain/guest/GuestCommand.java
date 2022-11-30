package kr.objet.okrproject.domain.guest;

import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import lombok.Builder;
import lombok.Getter;

public class GuestCommand {
	private static final String GUEST_PREFIX = "guest_";

	@Getter
	public static class RegisterGuest {
		private final String uuid;
		private final String id;
		private final String name;
		private final String email;
		private final String imageUrl;
		private final ProviderType providerType;

		@Builder
		public RegisterGuest(UserInfo.Main userInfo) {
			this.uuid = TokenGenerator.randomCharacterWithPrefix(GUEST_PREFIX);
			this.id = userInfo.getId();
			this.name = userInfo.getName();
			this.email = userInfo.getEmail();
			this.imageUrl = userInfo.getProfileImage();
			this.providerType = userInfo.getProviderType();
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
