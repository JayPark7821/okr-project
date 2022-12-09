package kr.objet.okrproject.interfaces.project;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.jupiter.api.Test;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;

class ProjectSaveDtoTest {

	@Test
	void keyResult_3개_초과() throws Exception {
		//given
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 0, 5, 5, "yyyy-MM-dd");

		//when
		Set<ConstraintViolation<ProjectSaveDto>> validate = Validation.buildDefaultValidatorFactory()
			.getValidator()
			.validate(projectSaveDto);

		//then
		assertThat(validate.isEmpty()).isFalse();

	}

	@Test
	void 날짜_포멧_검증_실패() throws Exception {
		//given
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 0, 0, 3, "yyyyMMdd");

		//when
		Set<ConstraintViolation<ProjectSaveDto>> validate = Validation.buildDefaultValidatorFactory()
			.getValidator()
			.validate(projectSaveDto);

		//then
		assertThat(validate.isEmpty()).isFalse();

	}

	@Test
	void command_변환_실패_프로젝트_종료일자_이후_시작일자() throws Exception {
		//given
		ProjectSaveDto projectSaveDto = getProjectSaveDto(1, 5, 0, 3, "yyyy-MM-dd");

		//when
		//then
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			projectSaveDto::toCommand);

		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROJECT_SDT_EDT.getMessage());
	}

	@Test
	void command_변환_실패_프로젝트_종료일자가_오늘이전() throws Exception {
		//given
		ProjectSaveDto projectSaveDto = getProjectSaveDto(5, 4, 0, 3, "yyyy-MM-dd");

		//when
		//then
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			projectSaveDto::toCommand);

		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROJECT_END_DATE.getMessage());
	}

	private static ProjectSaveDto getProjectSaveDto(
		int sdtDays,
		int edtDays,
		int min,
		int max,
		String pattern
	) {
		String sdt = LocalDate.now().minusDays(sdtDays).format(DateTimeFormatter.ofPattern(pattern));
		String edt = LocalDate.now().minusDays(edtDays).format(DateTimeFormatter.ofPattern(pattern));

		ProjectSaveDto projectSaveDto = ProjectSaveDtoFixture.create(sdt, edt, min, max);
		return projectSaveDto;
	}
}