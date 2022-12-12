package kr.objet.okrproject.domain.keyresult.service.fixture;

import java.time.LocalDate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;

public class KeyResultCommandFixture {
	public static KeyResultCommand.RegisterKeyResultWithProject create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now())
			.collectionSizeRange(0, 5);

		return new EasyRandom(param).nextObject(KeyResultCommand.RegisterKeyResultWithProject.class);
	}
}
