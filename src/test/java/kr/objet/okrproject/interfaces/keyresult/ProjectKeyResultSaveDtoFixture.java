package kr.objet.okrproject.interfaces.keyresult;

import kr.objet.okrproject.interfaces.project.ProjectSaveDto;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import static org.jeasy.random.FieldPredicates.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProjectKeyResultSaveDtoFixture {
	public static ProjectKeyResultSaveDto create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectKeyResultSaveDto.class);
	}
}
