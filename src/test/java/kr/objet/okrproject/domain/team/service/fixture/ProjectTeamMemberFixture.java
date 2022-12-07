package kr.objet.okrproject.domain.team.service.fixture;


import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;

public class ProjectTeamMemberFixture {
	public static ProjectTeamMemberCommand.RegisterProjectTeamMember create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectTeamMemberCommand.RegisterProjectTeamMember.class);
	}
}
