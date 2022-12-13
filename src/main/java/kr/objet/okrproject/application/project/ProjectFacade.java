package kr.objet.okrproject.application.project;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.ProjectMasterInfo;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberService;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.project.SortType;
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
			new TeamMemberCommand.RegisterProjectLeader(projectMaster, user)
		);
		registerKeyResultsFromCommand(command, projectMaster);

		return projectMaster.getProjectMasterToken();
	}

	public Page<ProjectMasterInfo.Response> retrieveProject(
		SortType sortType,
		String includeFinishedProjectYN,
		User user,
		Pageable page
	) {

		Page<ProjectMaster> results =
			projectMasterService.retrieveProject(sortType, includeFinishedProjectYN, user, page);
		return results.map(r -> new ProjectMasterInfo.Response(r, user.getEmail()));
	}

	public ProjectMasterInfo.DetailResponse searchProjectDetail(String projectToken, User user) {
		ProjectMaster result = projectMasterService.retrieveProjectDetail(projectToken, user);
		return null;
	}

	private void registerKeyResultsFromCommand(
		ProjectMasterCommand.RegisterProjectMaster command,
		ProjectMaster projectMaster
	) {
		command.getKeyResults().forEach(keyResult -> {
			keyResultService.registerKeyResult(
				new KeyResultCommand.RegisterKeyResultWithProject(keyResult, projectMaster)
			);
		});
	}
}
