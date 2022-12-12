package kr.objet.okrproject.domain.project.service.fixture;

import java.time.LocalDate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;

public class ProjectMasterCommandFixture {
	public static ProjectMasterCommand.RegisterProjectMaster create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.now().minusDays(200),
				LocalDate.now());
		return new EasyRandom(param).nextObject(ProjectMasterCommand.RegisterProjectMaster.class);
	}
}
