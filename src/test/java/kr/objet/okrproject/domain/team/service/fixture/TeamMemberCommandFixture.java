package kr.objet.okrproject.domain.team.service.fixture;


import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.team.service.TeamMemberCommand;

public class TeamMemberCommandFixture {
	public static TeamMemberCommand.RegisterProjectLeader createLeader() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(TeamMemberCommand.RegisterProjectLeader.class);
	}

	public static TeamMemberCommand.InviteTeamMember createMember(int min, int max) {
		EasyRandomParameters param = new EasyRandomParameters()
				.collectionSizeRange(min, max);
		return new EasyRandom(param).nextObject(TeamMemberCommand.InviteTeamMember.class);
	}
}
