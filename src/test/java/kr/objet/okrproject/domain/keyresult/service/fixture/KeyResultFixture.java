package kr.objet.okrproject.domain.keyresult.service.fixture;

import static org.jeasy.random.FieldPredicates.*;

import java.lang.reflect.Field;
import java.util.function.Predicate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.project.ProjectMaster;

public class KeyResultFixture {
	public static KeyResult create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(KeyResult.class);
	}

	public static KeyResult create(ProjectMaster projectMaster) {
		Predicate<Field> projectMasterPredicate = named("projectMaster").and(ofType(ProjectMaster.class))
			.and(inClass(KeyResult.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.randomize(projectMasterPredicate, () -> projectMaster);

		return new EasyRandom(param).nextObject(KeyResult.class);
	}
}
