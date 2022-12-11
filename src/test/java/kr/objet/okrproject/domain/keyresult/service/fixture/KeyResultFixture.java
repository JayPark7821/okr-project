package kr.objet.okrproject.domain.keyresult.service.fixture;

import kr.objet.okrproject.domain.keyresult.KeyResult;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class KeyResultFixture {
	public static KeyResult create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(KeyResult.class);
	}
}
