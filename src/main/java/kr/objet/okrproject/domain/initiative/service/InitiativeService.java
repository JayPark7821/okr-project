package kr.objet.okrproject.domain.initiative.service;

import java.time.LocalDate;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.team.TeamMember;

public interface InitiativeService {
	void validateInitiativeDates(LocalDate sdt, LocalDate edt, KeyResult keyResult);

	Initiative registerInitiative(InitiativeCommand.registerInitiative command, KeyResult keyResult,
		TeamMember teamMember);
}
