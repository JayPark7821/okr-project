package kr.objet.okrproject.application.initiative;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.InitiativeInfo;
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

	public Page<InitiativeInfo.Response> searchInitiatives(String keyResultToken, User user, Pageable page) {
		KeyResult keyResult = keyResultService.validateKeyResultWithUser(keyResultToken, user);

		Page<Initiative> results = initiativeService.searchInitiatives(keyResultToken, user, page);

		return results.map(i -> {
			return new InitiativeInfo.Response(i, user);
		});
	}
}
