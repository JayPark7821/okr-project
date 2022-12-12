package kr.objet.okrproject.domain.project.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.enums.ProjectType;
import lombok.Getter;

public class ProjectMasterCommand {

	@Getter
	public static class RegisterProjectMaster{

		private final String name;
		private final LocalDate sdt;
		private final LocalDate edt;
		private final String objective;
		private final List<String> keyResults;


		public RegisterProjectMaster(String name, LocalDate sdt, LocalDate edt, String objective, List<String> keyResults) {
			this.name = name;
			this.sdt = sdt;
			this.edt = edt;
			this.objective = objective;
			this.keyResults = keyResults;
		}

		public ProjectMaster toEntity() {
			return ProjectMaster.builder()
				.name(this.name)
				.startDate(this.sdt)
				.endDate(this.edt)
				.type(ProjectType.SINGLE)
				.objective(this.objective)
				.progress(0)
				.build();

		}
	}
}
