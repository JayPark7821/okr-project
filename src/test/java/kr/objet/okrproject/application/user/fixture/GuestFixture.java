package kr.objet.okrproject.application.user.fixture;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.guest.Guest;

public class GuestFixture {
	public static Guest create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(Guest.class);
	}
}
