package kr.objet.okrproject.domain.project;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.keyresult.KeyResult;
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
		private final List<TeamMember> teamMembers;
		private final String projectType;

		public Response(ProjectMaster entity, String email) {
			this.projectToken = entity.getProjectMasterToken();
			this.name = entity.getName();
			this.objective = entity.getObjective();
			this.progress = entity.getProgress();
			this.sdt = entity.getStartDate();
			this.edt = entity.getEndDate();
			this.projectType = entity.getType().getCode();
			this.teamMembers = entity.getTeamMember();
			this.newProject = entity.getTeamMember().stream()
				.filter(t -> t.getUser().getEmail().equals(email))
				.findFirst()
				.map(TeamMember::isNew)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
		}
	}

	@Getter
	public static class DetailResponse {

		private final String projectToken;

		private final String name;

		private final String objective;

		private final LocalDate sdt;

		private final LocalDate edt;

		private final List<KeyResult> keyResults;

		private final String projectType;

		public DetailResponse(ProjectMaster entity) {
			this.projectToken = entity.getProjectMasterToken();
			this.name = entity.getName();
			this.objective = entity.getObjective();
			this.sdt = entity.getStartDate();
			this.edt = entity.getEndDate();
			this.projectType = entity.getType().getCode();
			this.keyResults = entity.getKeyResults();
		}

	}

	@Getter
	public static class ProgressResponse {

		private final String dDay;
		private final String period;
		private final String progress;
		private final List<TeamMember> teamMembers;

		private final String projectType;

		public ProgressResponse(ProjectMaster entity) {
			long until = LocalDate.now().until(entity.getEndDate(), ChronoUnit.DAYS);
			this.dDay = "D" + (until >= 0 ? "-" + until : "+" + until * -1);
			this.period = entity.getStartDate() + "-" + entity.getEndDate();
			this.progress = Double.toString(entity.getProgress());
			this.projectType = entity.getType().getCode();
			this.teamMembers = entity.getTeamMember();
		}
	}

	@Getter
	public static class CalendarResponse {
		private final Long id;

		private final String name;

		private final String objective;

		private final double progress;

		private final LocalDate sdt;

		private final LocalDate edt;

		private final String projectType;

		public CalendarResponse(ProjectMaster entity) {
			this.id = entity.getId();
			this.name = entity.getName();
			this.objective = entity.getObjective();
			this.progress = entity.getProgress();
			this.sdt = entity.getStartDate();
			this.edt = entity.getEndDate();
			this.projectType = entity.getType().getCode();
		}
	}
}
