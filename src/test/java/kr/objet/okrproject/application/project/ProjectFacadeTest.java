package kr.objet.okrproject.application.project;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.ProjectMasterInfo;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterCommandFixture;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberService;
import kr.objet.okrproject.domain.user.User;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectFacadeTest {

	private ProjectFacade sut;

	@Mock
	private ProjectMasterService projectMasterService;

	@Mock
	private TeamMemberService teamMemberService;

	@Mock
	private KeyResultService keyResultService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new ProjectFacade(
			projectMasterService,
			teamMemberService,
			keyResultService
		);
	}

	@Test
	void 신규_프로젝트_등록_성공() throws Exception {

		//given
		ProjectMasterCommand.RegisterProjectMaster projectMasterCommand = ProjectMasterCommandFixture.create();
		ProjectMaster projectMaster = projectMasterCommand.toEntity();
		User user = UserFixture.create();

		given(projectMasterService.registerProjectMaster(projectMasterCommand)).willReturn(projectMaster);
		//when
		String projectToken = assertDoesNotThrow(() -> sut.registerProject(projectMasterCommand, user));

		//TODO : 이런경우에 어떻게 검증하는지 궁금합니다.
		//then
		then(projectMasterService).should(times(1))
			.registerProjectMaster(any(ProjectMasterCommand.RegisterProjectMaster.class));
		then(teamMemberService).should(times(1))
			.registerProjectTeamMember(any(TeamMemberCommand.RegisterProjectLeader.class));
		then(keyResultService).should(times(projectMasterCommand.getKeyResults().size()))
			.registerKeyResult(any(KeyResultCommand.RegisterKeyResultWithProject.class));
	}

	@Test
	void Info객체변환_테스트() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(LocalDate.now().minusDays(5),
			LocalDate.now().plusDays(10));

		given(projectMasterService.retrieveProjectProgress(eq(projectMaster.getProjectMasterToken()),
			any(User.class))).willReturn(projectMaster);
		//when
		ProjectMasterInfo.ProgressResponse result = assertDoesNotThrow(
			() -> sut.searchProjectProgressDetail(projectMaster.getProjectMasterToken(), mock(User.class)));
		//then
		assertThat(result.getDDay()).isEqualTo("D-10");
	}

	@Test
	void Info객체변환_테스트_D_plus_DAY() throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create(LocalDate.now().minusDays(5),
			LocalDate.now().minusDays(2));

		given(projectMasterService.retrieveProjectProgress(eq(projectMaster.getProjectMasterToken()),
			any(User.class))).willReturn(projectMaster);
		//when
		ProjectMasterInfo.ProgressResponse result = assertDoesNotThrow(
			() -> sut.searchProjectProgressDetail(projectMaster.getProjectMasterToken(), mock(User.class)));
		//then
		assertThat(result.getDDay()).isEqualTo("D+2");
	}
}