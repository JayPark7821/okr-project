package kr.objet.okrproject.application.keyresult;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.keyresult.KeyResult;
import kr.objet.okrproject.domain.keyresult.service.KeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.KeyResultService;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.keyresult.KeyResultSaveDtoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

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

        KeyResultCommand.RegisterKeyResultWithProject saveCommand =
                new KeyResultCommand.RegisterKeyResultWithProject(command.getName(), projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        given(keyResultService.registerKeyResult(
                eq(new KeyResultCommand.RegisterKeyResultWithProject(eq(command.getName()), eq(projectMaster)))
                )).willReturn(eq(keyResult));

        //when
        String keyResultToken = assertDoesNotThrow(() -> sut.registerKeyResult(command,user));


        //then
//        then(projectMasterService).should(times(1))
//                .registerProjectMaster(any(ProjectMasterCommand.RegisterProjectMaster.class));
//        then(projectKeyResultService).should(times(1))
//                .registerProjectKeyResult(any(ProjectKeyResultCommand.RegisterProjectKeyResultWithProject.class));
    }
}