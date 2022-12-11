package kr.objet.okrproject.application.keyresult;

import kr.objet.okrproject.application.project.ProjectFacade;
import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.keyresult.ProjectKeyResult;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultCommand;
import kr.objet.okrproject.domain.keyresult.service.ProjectKeyResultService;
import kr.objet.okrproject.domain.keyresult.service.fixture.ProjectKeyResultFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterCommand;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterCommandFixture;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.interfaces.keyresult.ProjectKeyResultSaveDto;
import kr.objet.okrproject.interfaces.keyresult.ProjectKeyResultSaveDtoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class KeyResultFacadeTest {

    private KeyResultFacade sut;

    @Mock
    private ProjectMasterService projectMasterService;

    @Mock
    private ProjectKeyResultService projectKeyResultService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new KeyResultFacade(
                projectKeyResultService,
                projectMasterService
        );
    }

    @Test
    void 신규_keyResult_등록_성공() throws Exception {

    }
}