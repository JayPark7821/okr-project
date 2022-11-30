package kr.objet.okrproject.domain.guest;

import kr.objet.okrproject.domain.user.enums.ProviderType;
import lombok.Getter;

public class GuestInfo {

	@Getter
	public static class Main {
		private final String guestUuid;
		private final String guestId;
		private final String guestName;
		private final String email;
		private final ProviderType providerType;
		private final String profileImageUrl;

		public Main(Guest guest) {
			this.guestUuid = guest.getGuestUuid();
			this.guestId = guest.getGuestId();
			this.guestName = guest.getGuestName();
			this.email = guest.getEmail();
			this.providerType = guest.getProviderType();
			this.profileImageUrl = guest.getProfileImageUrl();
		}
	}
}
