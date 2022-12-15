package kr.objet.okrproject.domain.project.service.fixture;

import static org.jeasy.random.FieldPredicates.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Predicate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;

public class ProjectMasterFixture {
	public static ProjectMaster create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now());
		return new EasyRandom(param).nextObject(ProjectMaster.class);
	}

	public static ProjectMaster create(List<TeamMember> teamMember) {
		Predicate<Field> teamMemberPredicate = named("teamMember").and(ofType(TeamMember.class))
			.and(inClass(ProjectMaster.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now())
			.randomize(teamMemberPredicate, () -> teamMember);
		ProjectMaster projectMaster = new EasyRandom(param).nextObject(ProjectMaster.class);

		teamMember.forEach(projectMaster::addTeamMember);
		return projectMaster;
	}

	public static ProjectMaster create(TeamMember teamMember) {
		Predicate<Field> teamMemberPredicate = named("teamMember").and(ofType(TeamMember.class))
			.and(inClass(ProjectMaster.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now())
			.randomize(teamMemberPredicate, () -> teamMember);

		return new EasyRandom(param).nextObject(ProjectMaster.class).addTeamMember(teamMember);
	}

	public static ProjectMaster create(LocalDate sdt, LocalDate edt) {
		Predicate<Field> endDatePredicate = named("endDate").and(ofType(LocalDate.class))
			.and(inClass(ProjectMaster.class));
		Predicate<Field> startDatePredicate = named("startDate").and(ofType(LocalDate.class))
			.and(inClass(ProjectMaster.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.randomize(endDatePredicate, () -> edt)
			.randomize(startDatePredicate, () -> sdt);
		return new EasyRandom(param).nextObject(ProjectMaster.class);
	}
}
