package kr.objet.okrproject.application.project;

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

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterCommandFixture;
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
		ProjectMaster projectMaster = projectMasterCommand.toEntity();
		User user = UserFixture.create();

		given(projectMasterService.registerProjectMaster(projectMasterCommand)).willReturn(projectMaster);
		//when
		Long projectId = assertDoesNotThrow(() -> sut.registerProject(projectMasterCommand, user));

		//TODO : 이런경우에 어떻게 검증하는지 궁금합니다.
		// ID setter 만들어서 검증??
		//then
		// assertEquals(projectMaster.getName());
	}
}