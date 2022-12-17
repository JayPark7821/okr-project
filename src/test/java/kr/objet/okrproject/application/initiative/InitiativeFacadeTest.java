package kr.objet.okrproject.application.initiative;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.initiative.Initiative;
import kr.objet.okrproject.domain.initiative.service.InitiativeCommand;
import kr.objet.okrproject.domain.initiative.service.InitiativeService;
import kr.objet.okrproject.domain.initiative.service.fixture.InitiativeCommandFixture;
import kr.objet.okrproject.domain.initiative.service.fixture.InitiativeFixture;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.service.fixture.TeamMemberFixture;
import kr.objet.okrproject.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.doNothing;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class InitiativeFacadeTest {

	private InitiativeFacade sut;

	@Mock
	private KeyResultService keyResultService;

	@Mock
	private InitiativeService initiativeService;

	@Mock
	private ProjectMasterService projectMasterService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new InitiativeFacade(
			keyResultService,
			initiativeService,
			projectMasterService
		);
	}

	@Test
	void 신규_initiative_등록_성공() throws Exception {
		InitiativeCommand.registerInitiative command = InitiativeCommandFixture.create();
		User user = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER);
		ProjectMaster projectMaster = ProjectMasterFixture.create(LocalDate.now().minusDays(10), LocalDate.now());
		projectMaster.addTeamMember(member);
		KeyResult keyResult = KeyResultFixture.create(projectMaster);
		Initiative initiative = InitiativeFixture.create();

		//given
		given(keyResultService.validateKeyResultWithUser(command.getKeyResultToken(), user))
			.willReturn(keyResult);
		given(initiativeService.registerInitiative(command, keyResult,
			keyResult.getProjectMaster().getTeamMember().get(0)))
			.willReturn(initiative);
		doNothing().when(projectMasterService)
				.updateProgress(projectMaster);
		//when
		String initiativeToken = assertDoesNotThrow(
			() -> sut.registerInitiative(command, user));

		//then
		assertThat(initiativeToken).isEqualTo(initiative.getInitiativeToken());
	}

	@Test
	void 신규_initiative_등록_실패_프로젝트_참여X() throws Exception {
		InitiativeCommand.registerInitiative command = InitiativeCommandFixture.create();
		User user = UserFixture.create();
		KeyResult keyResult = KeyResultFixture.create();
		Initiative initiative = InitiativeFixture.create();
		//given
		given(keyResultService.validateKeyResultWithUser(command.getKeyResultToken(), user))
			.willThrow(new OkrApplicationException(ErrorCode.INVALID_KEYRESULT_TOKEN));

		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.registerInitiative(command, user));

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_KEYRESULT_TOKEN.getMessage());
	}
}