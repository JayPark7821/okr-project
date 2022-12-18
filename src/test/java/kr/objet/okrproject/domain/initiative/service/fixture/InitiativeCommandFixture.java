package kr.objet.okrproject.domain.initiative.service.fixture;

import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

public class InitiativeCommandFixture {
	public static InitiativeCommand.RegisterInitiative create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now())
			.collectionSizeRange(0, 5);

		return new EasyRandom(param).nextObject(InitiativeCommand.RegisterInitiative.class);
	}
}
