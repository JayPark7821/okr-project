package kr.objet.okrproject.interfaces.team;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TeamMemberDto {

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

		public TeamMemberCommand.InviteTeamMember toCommand() {
			return TeamMemberCommand.InviteTeamMember.builder()
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
		private final List<String> addedEmailList;

		public saveResponse(String message, List<String> addedEmailList) {
			this.message = message;
			this.addedEmailList = addedEmailList;
		}
	}

}
