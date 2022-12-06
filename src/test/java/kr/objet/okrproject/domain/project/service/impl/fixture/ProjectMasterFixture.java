package kr.objet.okrproject.domain.project.service.impl.fixture;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.guest.GuestCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;

public class ProjectMasterFixture {
	public static ProjectMasterCommand.RegisterProjectMaster create() {
		EasyRandomParameters param = new EasyRandomParameters();
		return new EasyRandom(param).nextObject(ProjectMasterCommand.RegisterProjectMaster.class);
	}
}
