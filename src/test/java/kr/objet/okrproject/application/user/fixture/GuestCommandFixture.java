package kr.objet.okrproject.application.user.fixture;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.guest.Guest;
import kr.objet.okrproject.domain.guest.GuestCommand;

public class GuestCommandFixture {
	public static GuestCommand.Join create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(GuestCommand.Join.class);
	}
}
