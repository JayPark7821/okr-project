package kr.objet.okrproject.domain.project.service.fixture;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

import java.time.LocalDate;

public class ProjectMasterFixture {
	public static ProjectMaster create() {
		EasyRandomParameters param = new EasyRandomParameters()
			.dateRange(LocalDate.of(2022, 1, 1),
				LocalDate.of(2022, 12, 1));
		return new EasyRandom(param).nextObject(ProjectMaster.class);
	}
}
