package kr.objet.okrproject.domain.guest;

import kr.objet.okrproject.domain.user.enums.ProviderType;
import lombok.Builder;
import lombok.Getter;

public class GuestCommand {

	@Getter
	public static class RegisterGuest {
		private final String uuid;
		private final String id;
		private final String name;
		private final String email;
		private final String imageUrl;
		private final ProviderType providerType;

		@Builder
		public RegisterGuest(String uuid, String id, String name, String email, String imageUrl,
			ProviderType providerType) {
			this.uuid = uuid;
			this.id = id;
			this.name = name;
			this.email = email;
			this.imageUrl = imageUrl;
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
