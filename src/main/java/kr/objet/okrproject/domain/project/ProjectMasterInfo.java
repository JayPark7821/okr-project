package kr.objet.okrproject.domain.project;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.team.TeamMember;
import lombok.Getter;

public class ProjectMasterInfo {

	@Getter
	public static class Response {

		private final String projectToken;
		private final String name;
		private final String objective;
		private final boolean newProject;
		private final double progress;
		private final LocalDate sdt;
		private final LocalDate edt;
		private List<String> teamMemberEmails = new ArrayList<>();
		private List<String> teamMemberProfileImages = new ArrayList<>();
		private final String projectType;

		public Response(ProjectMaster entity, String email) {
			this.projectToken = entity.getProjectMasterToken();
			this.name = entity.getName();
			this.objective = entity.getObjective();
			this.progress = entity.getProgress();
			this.sdt = entity.getStartDate();
			this.edt = entity.getEndDate();
			this.projectType = entity.getType().getCode();

			for (TeamMember teamMember : entity.getTeamMember()) {
				this.teamMemberEmails.add(teamMember.getUser().getEmail());
				this.teamMemberProfileImages.add(teamMember.getUser().getProfileImageUrl());
			}
			this.newProject = entity.getTeamMember().stream()
				.filter(t -> t.getUser().getEmail().equals(email))
				.findFirst()
				.map(TeamMember::isNew)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
		}
	}

	public static class DetailResponse {
	}
}
