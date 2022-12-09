package kr.objet.okrproject.application.team;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

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
        ProjectTeamMemberCommand.InviteProjectTeamMember member
                = ProjectTeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(member.getProjectToken(), user))
                .willReturn();

        //when
        assertDoesNotThrow(() -> sut.inviteTeamMembers(member,user));

        //then
        then(projectMasterService).should(times(1))
                .validateProjectMasterWithUser(any(String.class), any(User.class));

    }


}