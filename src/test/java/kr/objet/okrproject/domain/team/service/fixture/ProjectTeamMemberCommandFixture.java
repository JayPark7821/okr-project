package kr.objet.okrproject.domain.team.service.fixture;


import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;

public class ProjectTeamMemberCommandFixture {
	public static ProjectTeamMemberCommand.RegisterProjectLeader createLeader() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectTeamMemberCommand.RegisterProjectLeader.class);
	}

	public static ProjectTeamMemberCommand.InviteProjectTeamMember createMember(int min, int max) {
		EasyRandomParameters param = new EasyRandomParameters()
				.collectionSizeRange(min, max);
		return new EasyRandom(param).nextObject(ProjectTeamMemberCommand.InviteProjectTeamMember.class);
	}
}
