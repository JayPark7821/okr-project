package kr.objet.okrproject.domain.team.service.fixture;


import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;

public class ProjectTeamMemberCommandFixture {
	public static ProjectTeamMemberCommand.RegisterProjectLeader create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectTeamMemberCommand.RegisterProjectLeader.class);
	}
}
