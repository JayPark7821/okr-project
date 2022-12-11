package kr.objet.okrproject.domain.keyresult.service.fixture;

import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

public class ProjectKeyResultFixture {
	public static ProjectKeyResult create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectKeyResult.class);
	}
}
