package kr.objet.okrproject.domain.project.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.common.utils.TokenGenerator;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.project.service.ProjectMasterStore;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterCommandFixture;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.user.User;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectMasterServiceImplTest {

	private ProjectMasterServiceImpl sut;

	@Mock
	private ProjectMasterStore projectMasterStore;

	@Mock
	private ProjectMasterReader projectMasterReader;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new ProjectMasterServiceImpl(projectMasterStore, projectMasterReader);
	}

	@Test
	void 프로젝트_등록_성공() throws Exception {
		//given
		ProjectMasterCommand.RegisterProjectMaster command = ProjectMasterCommandFixture.create();
		ProjectMaster projectMaster = command.toEntity();

		given(projectMasterStore.store(any())).willReturn(projectMaster);

		//when
		ProjectMaster savedProjectMaster = assertDoesNotThrow(() -> sut.registerProjectMaster(command));

		//then
		assertEquals(command.getName(), savedProjectMaster.getName());

	}

	@Test
	void 유저가_속해있는_프로젝트_조회_성공() throws Exception {
		//given
		String token = TokenGenerator.randomCharacterWithPrefix("mst_");
		User user = UserFixture.create();

		given(projectMasterReader.findByProjectTokenAndUser(eq(token), eq(user))).willReturn(
			Optional.of(mock(ProjectMaster.class)));

		//when
		ProjectMaster savedProjectMaster = assertDoesNotThrow(() -> sut.validateProjectMasterWithUser(token, user));
	}

	@Test
	void 유저가_속해있는_프로젝트_조회_실패() throws Exception {
		//given
		String token = TokenGenerator.randomCharacterWithPrefix("mst_");
		User user = UserFixture.create();

		given(projectMasterReader.findByProjectTokenAndUser(eq(token), eq(user))).willReturn(Optional.empty());

		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.validateProjectMasterWithUser(token, user));

		//then
		assertEquals(exception.getMessage(), ErrorCode.INVALID_PROJECT_TOKEN.getMessage());

	}

	@Test
	void 프로젝트마김일_검증_성공() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(
			LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(0)
		);
		//when
		assertDoesNotThrow(() -> sut.validateProjectDueDate(projectMaster));
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
			() -> sut.validateProjectDueDate(projectMaster));
		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROJECT_END_DATE.getMessage());
	}
}