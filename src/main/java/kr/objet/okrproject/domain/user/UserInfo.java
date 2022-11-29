package kr.objet.okrproject.domain.user;

import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.enums.RoleType;
import kr.objet.okrproject.domain.user.enums.jobtype.JobFieldDetail;
import lombok.Getter;

public class UserInfo {

	@Getter
	public static class Main {
		private final String email;
		private final String name;
		private final ProviderType providerType;
		private final RoleType roleType;
		private final JobFieldDetail jobFieldDetail;
		private final String profileImage;

		public Main(User user) {
			this.email = user.getEmail();
			this.name = user.getUsername();
			this.providerType = user.getProviderType();
			this.roleType = user.getRoleType();
			this.jobFieldDetail = user.getJobField();
			this.profileImage = user.getProfileImageUrl();
		}
	}

}
