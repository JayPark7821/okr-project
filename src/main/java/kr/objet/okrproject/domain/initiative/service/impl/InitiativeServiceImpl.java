package kr.objet.okrproject.domain.initiative.service.impl;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import kr.objet.okrproject.domain.initiative.service.InitiativeReader;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
import kr.objet.okrproject.domain.initiative.service.InitiativeStore;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class InitiativeServiceImpl implements InitiativeService {

	private final InitiativeReader initiativeReader;
	private final InitiativeStore initiativeStore;

	@Override
	public void validateInitiativeDates(LocalDate sdt, LocalDate edt, ProjectMaster projectMaster) {

		if (edt.isBefore(projectMaster.getStartDate()) ||
			edt.isAfter(projectMaster.getEndDate()) ||
			sdt.isBefore(projectMaster.getStartDate()) ||
			sdt.isAfter(projectMaster.getEndDate())
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

		ProjectMaster projectMaster = keyResult.getProjectMaster();
		projectMaster.validateProjectDueDate();
		validateInitiativeDates(command.getSdt(), command.getEdt(), projectMaster);

		return initiativeStore.store(command.toEntity(keyResult, teamMember));
	}

	@Override
	public Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page) {
		return initiativeReader.searchInitiatives(keyResultToken, user, page);
	}

	@Override
	public List<Initiative> searchInitiativesByDate(LocalDate searchDate, User user) {
		return initiativeReader.searchInitiativesByDate(searchDate, user);
	}

	@Override
	public List<String> searchActiveInitiativesByDate(YearMonth searchYearMonth, User user) {
		LocalDate monthEndDt = searchYearMonth.atEndOfMonth();
		LocalDate monthStDt = monthEndDt.minusDays(monthEndDt.lengthOfMonth() - 1);

		List<Initiative> initiatives = initiativeReader.searchActiveInitiativesByDate(monthEndDt, monthStDt, user);
		return initiatives.stream()
			.map(i -> getFromDate(monthStDt, i)
				.datesUntil(getToDate(monthEndDt, i))
				.map(LocalDate::toString)
				.collect(Collectors.toList()))
			.flatMap(Collection::stream)
			.distinct()
			.collect(Collectors.toList());
	}

	@Override
	public Initiative validateInitiativeForFeedback(String initiativeToken) {
		Initiative initiative = initiativeReader.findByInitiativeToken(initiativeToken)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));
		if (!initiative.isDone()) {
			throw new OkrApplicationException(ErrorCode.INITIATIVE_IS_NOT_FINISHED);
		}
		return initiative;
	}

	@Override
	public Initiative validateUserWithProjectMasterToken(String token, User user) {
		return initiativeReader.findByInitiativeTokenAndUser(token , user)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));
	}

	@Override
	public Integer getCountForFeedbackToGive(User user) {
		return initiativeReader.getCountForFeedbackToGive(user);
	}


	@Override
	public Initiative findByInitiativeToken(String initiativeToken) {
		return initiativeReader.findByInitiativeToken(initiativeToken)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));
	}

	private static LocalDate getToDate(LocalDate monthEndDt, Initiative i) {
		return (i.getEdt().isBefore(monthEndDt) ? i.getEdt() : monthEndDt).plusDays(1);
	}

	private static LocalDate getFromDate(LocalDate monthStDt, Initiative i) {
		return i.getSdt().isAfter(monthStDt) ? i.getSdt() : monthStDt;
	}

}
