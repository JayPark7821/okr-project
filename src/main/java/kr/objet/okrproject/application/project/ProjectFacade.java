package kr.objet.okrproject.application.project;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFacade {

	private final ProjectMasterService projectMasterService;
	private final TeamMemberService teamMemberService;
	private final KeyResultService keyResultService;

	public String registerProject(ProjectMasterCommand.RegisterProjectMaster command, User user) {

		ProjectMaster projectMaster = projectMasterService.registerProjectMaster(command);

		teamMemberService.registerProjectTeamMember(
			new TeamMemberCommand.RegisterProjectLeader(projectMaster,
				user
			));

		command.getKeyResults().forEach(keyResult -> {
			keyResultService.registerKeyResult(
				new KeyResultCommand.RegisterKeyResultWithProject(keyResult, projectMaster)
			);
		});

		return projectMaster.getProjectMasterToken();
	}
}
