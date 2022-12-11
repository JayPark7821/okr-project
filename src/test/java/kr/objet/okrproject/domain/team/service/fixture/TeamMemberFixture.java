package kr.objet.okrproject.domain.team.service.fixture;


import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class TeamMemberFixture {
	public static TeamMember createMember(User user, ProjectRoleType projectRoleType) {
		Predicate<Field> userPredicate = named("user").and(ofType(User.class))
				.and(inClass(TeamMember.class));

		Predicate<Field> rolePredicate = named("projectRoleType").and(ofType(ProjectRoleType.class))
				.and(inClass(TeamMember.class));

		EasyRandomParameters param = new EasyRandomParameters()
				.randomize(userPredicate, () -> user)
				.randomize(rolePredicate, () -> projectRoleType);

		return new EasyRandom(param).nextObject(TeamMember.class);
	}

	public static TeamMember createMember(User user, ProjectRoleType projectRoleType, ProjectMaster projectMaster) {
		Predicate<Field> userPredicate = named("user").and(ofType(User.class))
				.and(inClass(TeamMember.class));

		Predicate<Field> projectMasterPredicate = named("projectMaster").and(ofType(ProjectMaster.class))
				.and(inClass(TeamMember.class));

		Predicate<Field> rolePredicate = named("projectRoleType").and(ofType(ProjectRoleType.class))
				.and(inClass(TeamMember.class));

		EasyRandomParameters param = new EasyRandomParameters()
				.randomize(userPredicate, () -> user)
				.randomize(projectMasterPredicate, () -> projectMaster)
				.randomize(rolePredicate, () -> projectRoleType);

		return new EasyRandom(param).nextObject(TeamMember.class);
	}

}
