package kr.objet.okrproject.application.initiative;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiativeFacade {

	private final KeyResultService keyResultService;
	private final InitiativeService initiativeService;
	private final ProjectMasterService projectMasterService;

	public String registerInitiative(InitiativeCommand.registerInitiative command, User user) {
		KeyResult keyResult = keyResultService.validateKeyResultWithUser(command.getKeyResultToken(), user);
		keyResult.getProjectMaster().validateProjectDueDate();
		initiativeService.validateInitiativeDates(command.getSdt(), command.getEdt(), keyResult);
		Initiative initiative = initiativeService.registerInitiative(
			command,
			keyResult,
			keyResult.getProjectMaster().getTeamMember().get(0)
		);

		return initiative.getInitiativeToken();

	}
}
