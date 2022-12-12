package kr.objet.okrproject.domain.initiative.service.impl;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import kr.objet.okrproject.domain.initiative.service.InitiativeReader;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
import kr.objet.okrproject.domain.initiative.service.InitiativeStore;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.team.TeamMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl implements InitiativeService {

	private final InitiativeReader initiativeReader;
	private final InitiativeStore initiativeStore;

	@Override
	public void validateInitiativeDates(LocalDate sdt, LocalDate edt, KeyResult keyResult) {

		if (edt.isBefore(keyResult.getProjectMaster().getStartDate()) ||
			edt.isAfter(keyResult.getProjectMaster().getEndDate()) ||
			sdt.isBefore(keyResult.getProjectMaster().getStartDate()) ||
			sdt.isAfter(keyResult.getProjectMaster().getEndDate())
		) {
			throw new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_END_DATE);
		}
	}

	@Override
	public Initiative registerInitiative(
		InitiativeCommand.registerInitiative command,
		KeyResult keyResult,
		TeamMember teamMember
	) {

		Initiative initiative = command.toEntity(keyResult, teamMember);
		return initiativeStore.store(initiative);
	}
}
