package kr.objet.okrproject.domain.team.service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

public class ProjectTeamMemberCommand {

	@Getter
	public static class RegisterProjectLeader {

		private final ProjectMaster projectMaster;
		private final User user;

		@Builder
		public RegisterProjectLeader(
			ProjectMaster projectMaster,
			User user
		) {
			this.projectMaster = projectMaster;
			this.user = user;
		}

		public ProjectTeamMember toEntity() {
			return ProjectTeamMember.builder()
				.projectMaster(this.projectMaster)
				.user(this.user)
				.projectRoleType(ProjectRoleType.LEADER)
				.isNew(true)
				.build();
		}
	}

	@Getter
	public static class InviteProjectTeamMember{
		private final String projectToken;
		private final List<String> userEmails;

		@Builder
		public InviteProjectTeamMember(
				String projectToken,
				List<String> userEmails
		) {
			this.projectToken = projectToken;
			this.userEmails = userEmails;
		}

		public ProjectTeamMember toEntity(ProjectMaster projectMaster,User user ) {
			return ProjectTeamMember.builder()
					.projectMaster(projectMaster)
					.user(user)
					.projectRoleType(ProjectRoleType.MEMBER)
					.isNew(true)
					.build();
		}
	}


}
