package kr.objet.okrproject.application.project;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
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
	private final ProjectKeyResultService projectKeyResultService;

	public String registerProject(ProjectMasterCommand.RegisterProjectMaster command, User user) {

		ProjectMaster projectMaster = projectMasterService.registerProjectMaster(command);

		projectTeamMemberService.registerProjectTeamMember(
			new ProjectTeamMemberCommand.RegisterProjectLeader(projectMaster,
				user
			));

		command.getKeyResults().forEach(keyResult -> {
			projectKeyResultService.registerProjectKeyResult(
				new ProjectKeyResultCommand.RegisterProjectKeyResultWithProject(keyResult, projectMaster)
			);
		});

		return projectMaster.getProjectMasterToken();
	}
}
