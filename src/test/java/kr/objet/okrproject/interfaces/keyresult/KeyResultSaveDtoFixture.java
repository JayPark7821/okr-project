package kr.objet.okrproject.interfaces.keyresult;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;

public class KeyResultSaveDtoFixture {
	public static KeyResultSaveDto create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(KeyResultSaveDto.class);
	}
}
