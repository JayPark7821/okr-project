package kr.objet.okrproject.interfaces.project;

import static org.jeasy.random.FieldPredicates.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.function.Predicate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class ProjectSaveDtoFixture {
	public static ProjectSaveDto create(String sdt, String edt) {
		Predicate<Field> sdtPredicate = named("sdt").and(ofType(String.class))
			.and(inClass(ProjectSaveDto.class));
		Predicate<Field> edtPredicate = named("edt").and(ofType(String.class))
			.and(inClass(ProjectSaveDto.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.of(2022, 1, 1),
				LocalDate.of(2022, 12, 1))
			.randomize(sdtPredicate, () -> sdt)
			.randomize(edtPredicate, () -> edt);
		return new EasyRandom(param).nextObject(ProjectSaveDto.class);
	}
}
