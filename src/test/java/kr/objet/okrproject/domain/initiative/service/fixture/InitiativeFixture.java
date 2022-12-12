package kr.objet.okrproject.domain.initiative.service.fixture;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.initiative.Initiative;

public class InitiativeFixture {
	public static Initiative create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(Initiative.class);
	}
}
