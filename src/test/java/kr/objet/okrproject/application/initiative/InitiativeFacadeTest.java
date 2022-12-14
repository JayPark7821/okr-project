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
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

	@Mock
	private NotificationService notificationService;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new InitiativeFacade(
			keyResultService,
			initiativeService,
			projectMasterService,
			notificationService

		);
	}

	@Test
	void ??????_initiative_??????_??????_????????????_??????X() throws Exception {
		InitiativeCommand.RegisterInitiative command = InitiativeCommandFixture.create();
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