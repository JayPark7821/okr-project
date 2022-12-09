package kr.objet.okrproject.interfaces.project;

import static kr.objet.okrproject.interfaces.project.ProjectSaveDtoFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
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

}