package kr.objet.okrproject.application.keyresult;

import static org.assertj.core.api.Assertions.*;
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
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.keyresult.KeyResultSaveDtoFixture;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class KeyResultFacadeTest {

    private KeyResultFacade sut;

    @Mock
    private ProjectMasterService projectMasterService;

    @Mock
    private KeyResultService keyResultService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new KeyResultFacade(
            keyResultService,
            projectMasterService
        );
    }

    @Test
    void 신규_keyResult_등록_성공() throws Exception {
        //given
        KeyResultCommand.RegisterKeyResult command = KeyResultSaveDtoFixture.create().toCommand();
        User user = UserFixture.create();

        ProjectMaster projectMaster = ProjectMasterFixture.create();
        KeyResult keyResult = KeyResultFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
            .willReturn(projectMaster);
        given(keyResultService.registerKeyResult(any(KeyResultCommand.RegisterKeyResultWithProject.class)))
            .willReturn(keyResult);

        //when
        String keyResultToken = assertDoesNotThrow(() -> sut.registerKeyResult(command, user));

        //then
        assertThat(keyResultToken).isEqualTo(keyResult.getKeyResultToken());

    }

    @Test
    void keyResult_등록_실패_저장_요청자_프로젝트_참여자X() throws Exception {
        //given
        KeyResultCommand.RegisterKeyResult command = KeyResultSaveDtoFixture.create().toCommand();
        User user = UserFixture.create();

        ProjectMaster projectMaster = ProjectMasterFixture.create();
        KeyResult keyResult = KeyResultFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
            .willThrow(new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN));

        //when
        OkrApplicationException exception = assertThrows(OkrApplicationException.class,
            () -> sut.registerKeyResult(command, user));
        //then

        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
    }
}