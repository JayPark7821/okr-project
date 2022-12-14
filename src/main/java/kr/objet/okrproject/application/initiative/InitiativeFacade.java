package kr.objet.okrproject.application.initiative;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

		return results.map(i -> new InitiativeInfo.Response(i, user));
	}

	public List<InitiativeInfo.Response> searchInitiativesByDate(LocalDate searchDate, User user) {
		List<Initiative> results = initiativeService.searchInitiativesByDate(searchDate, user);

		return results.stream()
				.map(i -> new InitiativeInfo.Response(i, user))
				.collect(Collectors.toList());
	}

}
