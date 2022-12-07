package kr.objet.okrproject.application.project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import kr.objet.okrproject.application.user.UserFacade;
import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.impl.fixture.ProjectMasterCommandFixture;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.user.User;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectFacadeTest {

	private ProjectFacade sut;

	@Mock
	private ProjectMasterService projectMasterService;

	@Mock
	private ProjectTeamMemberService projectTeamMemberService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new ProjectFacade(
			projectMasterService,
			projectTeamMemberService
		);
	}

	@Test
	void 신규_프로젝트_등록_성공 () throws Exception {
	    //given
		ProjectMasterCommand.RegisterProjectMaster projectMasterCommand = ProjectMasterCommandFixture.create();
		User user = UserFixture.create();

		//when
		Long projectId = assertDoesNotThrow(() -> sut.registerProject(projectMasterCommand, user));

		//then
		assertNotNull(projectId);
	}
}