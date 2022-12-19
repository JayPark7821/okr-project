package kr.objet.okrproject.domain.initiative.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;

public interface InitiativeService {
	void validateInitiativeDates(LocalDate sdt, LocalDate edt, ProjectMaster projectMaster);

	Initiative registerInitiative(InitiativeCommand.RegisterInitiative command, KeyResult keyResult,
		TeamMember teamMember);

	Page<Initiative> searchInitiatives(String keyResultToken, User user, Pageable page);

	List<Initiative> searchInitiativesByDate(LocalDate searchDate, User user);

	List<String> searchActiveInitiativesByDate(YearMonth searchYearMonth, User user);

	Initiative findByInitiativeToken(String initiativeToken);

	Integer getCountForFeedbackToGive(User user);

	void setInitiativeStatusToDone(Initiative initiative, User user);

	Initiative validateInitiativeOwnerWithToken(String token, User user);

	Initiative updateInitiative(InitiativeCommand.UpdateInitiative request, String token, User user);
}
