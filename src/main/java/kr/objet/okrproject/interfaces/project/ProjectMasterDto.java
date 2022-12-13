package kr.objet.okrproject.interfaces.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.DateValid;
import kr.objet.okrproject.domain.project.ProjectMasterInfo;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProjectMasterDto {

	@Getter
	@NoArgsConstructor
	public static class Save {

		@Valid
		@NotNull(message = "프로젝트명은 필수 값입니다.")
		@Size(max = 50, message = "프로젝트명은 50자보다 클 수 없습니다.")
		@Schema(example = "프로젝트 명")
		private String name;

		@Valid
		@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
		@NotNull(message = "프로젝트 시작 일자는 필수 값입니다.")
		@Schema(example = "프로젝트 시작 일자 2022-09-01")
		private String sdt;

		@Valid
		@DateValid(message = "8자리의 yyyy-MM-dd 형식이어야 합니다.", pattern = "yyyy-MM-dd")
		@NotNull(message = "프로젝트 종료 일자는 필수 값입니다.")
		@Schema(example = "프로젝트 종료 일자 2022-09-09")
		private String edt;

		@Valid
		@NotNull(message = "프로젝트 objective는 필수 값입니다.")
		@Size(max = 50)
		@Schema(example = "프로젝트 objective")
		private String objective;

		@Valid
		@NotNull(message = "Key Result는 필수 값입니다.")
		@Size(max = 3, message = "Key Result는 3개 까지만 등록이 가능합니다.")
		@Schema(example = "프로젝트 Key Result List [kr1,kr2,kr3...]")
		private List<String> keyResults;

		@Builder
		public Save(String name, String sdt, String edt, String objective, List<String> keyResults) {
			this.name = name;
			this.sdt = sdt;
			this.edt = edt;
			this.objective = objective;
			this.keyResults = keyResults;
		}

		public ProjectMasterCommand.RegisterProjectMaster toCommand() {
			LocalDate startDt = LocalDate.parse(this.sdt, DateTimeFormatter.ISO_DATE);
			LocalDate endDt = LocalDate.parse(this.edt, DateTimeFormatter.ISO_DATE);

			if (!startDt.isBefore(endDt)) {
				throw new OkrApplicationException(ErrorCode.INVALID_PROJECT_SDT_EDT);
			}
			if (LocalDate.now().isAfter(endDt)) {
				throw new OkrApplicationException(ErrorCode.INVALID_PROJECT_END_DATE);
			}

			return new ProjectMasterCommand.RegisterProjectMaster(
				this.name,
				startDt,
				endDt,
				this.objective,
				this.keyResults
			);
		}
	}

	@Getter
	@NoArgsConstructor
	public static class Response {

		@Schema(description = "프로젝트 토큰", example = "125")
		private String projectToken;

		@Schema(description = "프로젝트 명", example = "OKR 프로젝트")
		private String name;

		@Schema(description = "프로젝트 objective", example = "OKR 서비스 애플리케이션을 개발한다.")
		private String objective;

		@Schema(description = "신규 프로젝트 여부", example = "true")
		private boolean newProject;

		@Schema(description = "진척도", example = "20%")
		private double progress;

		@Schema(description = "프로젝트 시작일자", example = "2022.08.11")
		private LocalDate sdt;

		@Schema(description = "프로젝트 종료일자", example = "2022.09.11")
		private LocalDate edt;

		@Schema(description = "프로젝트 참여자 email", example = "[test@gmail.com, test2@gmail.com]")
		private List<String> teamMemberEmails;

		@Schema(description = "프로젝트 참여자 프로필 이미지", example = "[]]")
		private List<String> teamMemberProfileImages;

		@Schema(description = "프로젝트 타입", example = "SINGLE, TEAM")
		private String projectType;

		public Response(ProjectMasterInfo.Response response) {
			this.projectToken = response.getProjectToken();
			this.name = response.getName();
			this.objective = response.getObjective();
			this.newProject = response.isNewProject();
			this.progress = response.getProgress();
			this.sdt = response.getSdt();
			this.edt = response.getEdt();
			this.teamMemberEmails = response.getTeamMemberEmails();
			this.teamMemberProfileImages = response.getTeamMemberProfileImages();
			this.projectType = response.getProjectType();
		}
	}

	@Getter
	@NoArgsConstructor
	public static class DetailResponse {

		@Schema(description = "프로젝트 토큰", example = "123")
		private String projectToken;

		@Schema(description = "프로젝트 명", example = "OKR프로젝트")
		private String name;

		@Schema(description = "프로젝트 objective", example = "OKR 기반 일정관리 어플을 출시하자!")
		private String objective;

		@Schema(description = "프로젝트 시작일자.", example = "2022.09.03")
		private LocalDate sdt;

		@Schema(description = "프로젝트 종료.", example = "2022.10.03")
		private LocalDate edt;

		@Schema(description = "key result list", example = "[{name}]")
		private List<ProjectKeyResultResponseDto> keyResults;

		@Schema(description = "프로젝트 타입", example = "SINGLE, TEAM")
		private String projectType;

		public DetailResponse(ProjectMasterInfo.DetailResponse response) {
			this.projectToken = response.getProjectToken();
			this.name = response.getName();
			this.objective = response.getObjective();
			this.sdt = response.getSdt();
			this.edt = response.getEdt();
			this.projectType = response.getProjectType();
			this.keyResults = response.getKeyResults()
				.stream()
				.map(ProjectKeyResultResponseDto::new)
				.collect(Collectors.toList());

		}

		private static class ProjectKeyResultResponseDto {

			private String keyResultToken;
			private String keyResultName;

			public ProjectKeyResultResponseDto(ProjectMasterInfo.DetailResponse.ProjectKeyResultInfo keyResult) {
				this.keyResultToken = keyResult.getKeyResultToken();
				this.keyResultName = keyResult.getName();
			}
		}
	}
}
