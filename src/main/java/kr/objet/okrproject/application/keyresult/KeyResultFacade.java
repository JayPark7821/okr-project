package kr.objet.okrproject.application.keyresult;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultFacade {
	private final ProjectKeyResultService projectKeyResultService;
	private final ProjectMasterService projectMasterService;

	public String registerKeyResult(ProjectKeyResultCommand.RegisterProjectKeyResult command, User user) {

		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user);

		ProjectKeyResult keyResult = projectKeyResultService.registerProjectKeyResult(
				new ProjectKeyResultCommand.RegisterProjectKeyResultWithProject(command.getName(), projectMaster)
		);
		return keyResult.getProjectKeyResultToken();
	}
}
