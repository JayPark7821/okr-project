package kr.objet.okrproject.domain.project.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.impl.fixture.ProjectMasterFixture;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectMasterServiceImplTest {

	private ProjectMasterServiceImpl sut;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new ProjectMasterServiceImpl();
	}

	@Test
	void 프로젝트_등록_성공() throws Exception {
	    //given
		ProjectMasterCommand.RegisterProjectMaster command = ProjectMasterFixture.create();

		//when
		ProjectMaster projectMaster = assertDoesNotThrow(() -> sut.registerProjectMaster(command));

		//then
		assertEquals(command.getName(), projectMaster.getName());
		assertNotNull(projectMaster.getProjectId());
	}
}