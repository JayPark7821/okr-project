package kr.objet.okrproject.application.keyresult;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeyResultFacade {
	private final KeyResultService keyResultService;
	private final ProjectMasterService projectMasterService;

	public String registerKeyResult(KeyResultCommand.RegisterKeyResult command, User user) {

		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user);

		KeyResult keyResult = keyResultService.registerKeyResult(
				new KeyResultCommand.RegisterKeyResultWithProject(command.getName(), projectMaster)
		);
		return keyResult.getKeyResultToken();
	}
}
