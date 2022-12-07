package kr.objet.okrproject.application.project;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectMasterService projectMasterService;
	private final ProjectTeamMemberService projectTeamMemberService;

	public Long registerProject(ProjectMasterCommand.RegisterProjectMaster command, User user) {
		ProjectMaster projectMaster = projectMasterService.registerProjectMaster(command);

		ProjectTeamMemberCommand.RegisterProjectTeamMember projectTeamMember =
			ProjectTeamMemberCommand.RegisterProjectTeamMember.builder()
				.projectMaster(projectMaster)
				.user(user)
				.isNew(true)
				.roleType(ProjectRoleType.LEADER)
				.build();

		projectTeamMemberService.registerProjectTeamMember(projectTeamMember);
		return projectMaster.getProjectId();
	}
}
