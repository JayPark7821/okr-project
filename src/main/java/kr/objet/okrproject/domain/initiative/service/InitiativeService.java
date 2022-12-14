package kr.objet.okrproject.domain.initiative.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;

public interface InitiativeService {
	void validateInitiativeDates(LocalDate sdt, LocalDate edt, KeyResult keyResult);

	Initiative registerInitiative(InitiativeCommand.registerInitiative command, KeyResult keyResult,
		TeamMember teamMember);

	Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page);
}
