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
	public static ProjectMasterDto.Save create(String sdt, String edt, int min, int max) {
		Predicate<Field> sdtPredicate = named("sdt").and(ofType(String.class))
			.and(inClass(ProjectMasterDto.Save.class));
		Predicate<Field> edtPredicate = named("edt").and(ofType(String.class))
			.and(inClass(ProjectMasterDto.Save.class));

		EasyRandomParameters param = new EasyRandomParameters()
			.collectionSizeRange(min, max)
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now())
			.randomize(sdtPredicate, () -> sdt)
			.randomize(edtPredicate, () -> edt);
		return new EasyRandom(param).nextObject(ProjectMasterDto.Save.class);
	}

	public static String getDateString(int calcDays, String pattern) {
		if (calcDays < 0) {
			return LocalDate.now().minusDays(calcDays * -1).format(DateTimeFormatter.ofPattern(pattern));
		} else {
			return LocalDate.now().plusDays(calcDays).format(DateTimeFormatter.ofPattern(pattern));
		}

	}
}
