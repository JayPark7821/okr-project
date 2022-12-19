package kr.objet.okrproject.domain.initiative.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
		InitiativeCommand.RegisterInitiative command,
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
	public Integer getCountForFeedbackToGive(User user) {
		return initiativeReader.getCountForFeedbackToGive(user);
	}

	@Transactional
	@Override
	public void setInitiativeStatusToDone(Initiative initiative, User user) {
		if (initiative.isDone()) {
			throw new OkrApplicationException(ErrorCode.ALREADY_FINISHED_INITIATIVE);
		}
		initiative.markInitiativeAsDone();
	}

	@Override
	public Initiative validateInitiativeOwnerWithToken(String token, User user) {
		return initiativeReader.validateInitiativeOwnerWithToken(token, user)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_INITIATIVE_TOKEN));
	}

	@Transactional
	@Override
	public Initiative updateInitiative(InitiativeCommand.UpdateInitiative request, String token, User user) {
		Initiative initiative = validateInitiativeOwnerWithToken(token, user);
		if (initiative.isDone()) {
			throw new OkrApplicationException(ErrorCode.ALREADY_FINISHED_INITIATIVE);
		}
		validateInitiativeDates(request.getSdt(), request.getEdt(), initiative.getKeyResult().getProjectMaster());
		initiative.updateInitiative(request.getIniDetail(), request.getSdt(), request.getEdt());
		return initiative;
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
