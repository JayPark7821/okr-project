package kr.objet.okrproject.domain.team.service;

import java.util.List;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.user.User;
import lombok.Builder;
import lombok.Getter;

public class ProjectTeamMemberCommand {

	@Getter
	public static class RegisterProjectTeamMember {

		private ProjectMaster projectMaster;
		private ProjectRoleType roleType;
		private boolean isNew;
		private User user;

		@Builder
		public RegisterProjectTeamMember(
			ProjectMaster projectMaster,
			ProjectRoleType roleType,
			boolean isNew,
			User user
		) {
			this.projectMaster = projectMaster;
			this.roleType = roleType;
			this.isNew = isNew;
			this.user = user;
		}

		public ProjectTeamMember toEntity() {
			return ProjectTeamMember.builder()
				.projectMaster(this.projectMaster)
				.user(this.user)
				.projectRoleType(this.roleType)
				.isNew(this.isNew)
				.build();
		}
	}



}
