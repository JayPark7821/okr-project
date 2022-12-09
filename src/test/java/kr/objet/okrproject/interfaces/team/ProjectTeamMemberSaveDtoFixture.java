package kr.objet.okrproject.interfaces.team;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class ProjectTeamMemberSaveDtoFixture {

	public static ProjectTeamMemberDto.saveRequest create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectTeamMemberDto.saveRequest.class);
	}

}
