package kr.objet.okrproject.application.team;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.team.service.fixture.ProjectTeamMemberCommandFixture;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.interfaces.team.ProjectTeamMemberSaveDtoFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectTeamMemberFacadeTest {

    private ProjectTeamMemberFacade sut;

    @Mock
    private ProjectMasterService projectMasterService;

    @Mock
    private ProjectTeamMemberService projectTeamMemberService;

    @Mock
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new ProjectTeamMemberFacade(
                projectMasterService,
                projectTeamMemberService,
                userService
        );
    }

    @Test
    void 신규_팀원_등록_성공() throws Exception {
        //given
        ProjectTeamMemberCommand.InviteProjectTeamMember command
                = ProjectTeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        doNothing().when(projectTeamMemberService).checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);
        given(userService.findUsersByEmails(command.getUserEmails()))
                .willReturn(List.of(mock(User.class)));

        //when
        assertDoesNotThrow(() -> sut.inviteTeamMembers(command,user));

        //then
        then(projectMasterService).should(times(1))
                .validateProjectMasterWithUser(any(String.class), any(User.class));

    }


}