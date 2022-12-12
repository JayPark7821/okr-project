package kr.objet.okrproject.domain.initiative.service.fixture;

import java.time.LocalDate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;

public class InitiativeCommandFixture {
	public static InitiativeCommand.registerInitiative create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now())
			.collectionSizeRange(0, 5);

		return new EasyRandom(param).nextObject(InitiativeCommand.registerInitiative.class);
	}
}
