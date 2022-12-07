package kr.objet.okrproject.domain.project.service.impl.fixture;

import java.time.LocalDate;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;

public class ProjectMasterCommandFixture {
	public static ProjectMasterCommand.RegisterProjectMaster create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.of(2022, 1, 1),
				LocalDate.of(2022, 12, 1));
		return new EasyRandom(param).nextObject(ProjectMasterCommand.RegisterProjectMaster.class);
	}
}
