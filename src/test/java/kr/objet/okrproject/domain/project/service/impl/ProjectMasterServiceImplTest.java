package kr.objet.okrproject.domain.project.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterReader;
import kr.objet.okrproject.domain.project.service.ProjectMasterStore;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterCommandFixture;

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
		sut = new ProjectMasterServiceImpl(projectMasterStore);
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
}