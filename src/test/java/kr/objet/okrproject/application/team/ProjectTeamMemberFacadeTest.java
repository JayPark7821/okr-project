package kr.objet.okrproject.application.team;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.ProjectTeamMemberSavedInfo;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.team.service.fixture.ProjectTeamMemberCommandFixture;
import kr.objet.okrproject.domain.team.service.fixture.ProjectTeamMemberFixture;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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
        ProjectTeamMember projectTeamMember = ProjectTeamMemberFixture.createMember(user, ProjectRoleType.LEADER, projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        given(userService.findUsersByEmails(command.getUserEmails()))
                .willReturn(List.of(user));
        given(projectTeamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)))
                .willReturn(List.of(projectTeamMember));
        given(projectTeamMemberService.checkUsersAndRegisterTeamMember(List.of(user), List.of(projectTeamMember), projectMaster))
                .willReturn(mock(ProjectTeamMemberSavedInfo.class));
        doNothing().when(projectTeamMemberService).checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);

        //when
        assertDoesNotThrow(() -> sut.inviteTeamMembers(command,user));

        //then
        then(projectMasterService).should(times(1))
                .validateProjectMasterWithUser(command.getProjectToken(), user);
        then(userService).should(times(1))
                .findUsersByEmails(command.getUserEmails());
        then(projectTeamMemberService).should(times(1))
                .findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user));
        then(projectTeamMemberService).should(times(1))
                .checkUsersAndRegisterTeamMember(List.of(user), List.of(projectTeamMember), projectMaster);
        then(projectTeamMemberService).should(times(1))
                .checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);

    }


    @Test
    void 신규_팀원_등록_실패_리더가아님() throws Exception {
        //given
        ProjectTeamMemberCommand.InviteProjectTeamMember command
                = ProjectTeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();
        ProjectTeamMember projectTeamMember = ProjectTeamMemberFixture.createMember(user, ProjectRoleType.MEMBER, projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        doThrow(new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN))
                .when(projectTeamMemberService).checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);


        //when
        assertThrows(OkrApplicationException.class,() -> sut.inviteTeamMembers(command,user));
    }

    @Test
    void 신규_팀원_등록_실패_추가된_팀원이_없음() throws Exception {
        //given
        ProjectTeamMemberCommand.InviteProjectTeamMember command
                = ProjectTeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();
        ProjectTeamMember projectTeamMember = ProjectTeamMemberFixture.createMember(user, ProjectRoleType.LEADER, projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        given(userService.findUsersByEmails(command.getUserEmails()))
                .willReturn(List.of(user));
        given(projectTeamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)))
                .willReturn(List.of(projectTeamMember));
        given(projectTeamMemberService.checkUsersAndRegisterTeamMember(List.of(user), List.of(projectTeamMember), projectMaster))
                .willThrow(new OkrApplicationException(ErrorCode.NO_USERS_ADDED));
        doNothing().when(projectTeamMemberService).checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);


        //when
        assertThrows(OkrApplicationException.class,() -> sut.inviteTeamMembers(command,user));
    }


    @Test
    void 초대할_팀원_유효성_검사_성공() throws Exception {
        //given
        String token = "projectToken";
        String email = "test@test.com";
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(token, user))
                .willReturn(projectMaster);
        doNothing().when(projectTeamMemberService).validateEmailWithProject(email, projectMaster);
        doNothing().when(userService).validateUserWithEmail(email);


        //when
        String validatedEmail = assertDoesNotThrow(() -> sut.validateEmail(token, email, user));

        //then
        assertThat(validatedEmail).isEqualTo(email);
    }
}