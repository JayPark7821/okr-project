package kr.objet.okrproject.domain.project.service.fixture;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class ProjectMasterFixture {
	public static ProjectMaster create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.of(2022, 1, 1),
				LocalDate.of(2022, 12, 1));
		return new EasyRandom(param).nextObject(ProjectMaster.class);
	}

	public static ProjectMaster create(TeamMember teamMember) {
		Predicate<Field> teamMemberPredicate = named("projectTeamMember").and(ofType(TeamMember.class))
				.and(inClass(ProjectMaster.class));

		EasyRandomParameters param = new EasyRandomParameters()
				.dateRange(LocalDate.of(2022, 1, 1),
						   LocalDate.of(2022, 12, 1))
				.randomize(teamMemberPredicate, () -> teamMember);
		return new EasyRandom(param).nextObject(ProjectMaster.class);
	}
}
