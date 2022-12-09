package kr.objet.okrproject.interfaces.project;

import static org.jeasy.random.FieldPredicates.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class ProjectSaveDtoFixture {
	private static ProjectSaveDto create(String sdt, String edt, int min, int max) {
		Predicate<Field> sdtPredicate = named("sdt").and(ofType(String.class))
			.and(inClass(ProjectSaveDto.class));
		Predicate<Field> edtPredicate = named("edt").and(ofType(String.class))
			.and(inClass(ProjectSaveDto.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.collectionSizeRange(min, max)
			.dateRange(LocalDate.of(2022, 1, 1),
				LocalDate.of(2022, 12, 1))
			.randomize(sdtPredicate, () -> sdt)
			.randomize(edtPredicate, () -> edt);
		return new EasyRandom(param).nextObject(ProjectSaveDto.class);
	}

	public static ProjectSaveDto getProjectSaveDto(int sdtDays, int edtDays, int min, int max, String pattern) {

		String sdt = LocalDate.now().minusDays(sdtDays).format(DateTimeFormatter.ofPattern(pattern));
		String edt = LocalDate.now().minusDays(edtDays).format(DateTimeFormatter.ofPattern(pattern));

		return create(sdt, edt, min, max);
	}

}
