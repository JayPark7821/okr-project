package kr.objet.okrproject.domain.project;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;

class ProjectMasterTest {

	@Test
	void 프로젝트마김일_검증_성공() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);
		//when
		assertDoesNotThrow(projectMaster::validateProjectDueDate);
		//then
	}

	@Test
	void 프로젝트마김일_검증_실패_프로젝트종료일자_지남() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(1)
		);
		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			projectMaster::validateProjectDueDate);

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROJECT_END_DATE.getMessage());
		
	}

}