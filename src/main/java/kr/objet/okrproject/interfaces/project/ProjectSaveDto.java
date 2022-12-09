package kr.objet.okrproject.interfaces.project;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.DateValid;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProjectSaveDto {

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
	public ProjectSaveDto(String name, String sdt, String edt, String objective, List<String> keyResults) {
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
