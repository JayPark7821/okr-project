package kr.objet.okrproject.interfaces.team;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class TeamMemberSaveDtoFixture {

	public static TeamMemberDto.saveRequest create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(TeamMemberDto.saveRequest.class);
	}

}
