package kr.objet.okrproject.interfaces.team;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectTeamMemberDto {

	@Getter
	@NoArgsConstructor
	public static class saveRequest {
		@Valid
		@NotNull(message = "프로젝트 token은 필수 값입니다.")
		@Schema(example = "프로젝트 token")
		private String projectToken;

		@Valid
		@NotNull(message = "상대방 email 주소는 필수 값입니다.")
		@Size(max = 100)
		@Schema(example = "[test@test.com, abc@abc.com ...]")
		private List<String> emails;

		public ProjectTeamMemberCommand.InviteProjectTeamMember toCommand() {
			return ProjectTeamMemberCommand.InviteProjectTeamMember.builder()
					.projectToken(this.projectToken)
					.userEmails(this.emails)
					.build();
		}

		public saveRequest(String projectToken, List<String> emails) {
			this.projectToken = projectToken;
			this.emails = emails;
		}
	}

	@Getter
	public static class saveResponse {
		private final String message;
		private List<String> failedEmailList = new ArrayList<>();
		private List<String> addedEmailList = new ArrayList<>();

		public saveResponse() {
			this.message = Notifications.PROJECT_TYPE_CHANGE.getMsg();
		}

		public void addEmailForAddedMember(String email) {
			addedEmailList.add(email);
		}

		public void addEmailForFailedMember(String email) {
			failedEmailList.add(email);
		}
	}

}
