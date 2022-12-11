package kr.objet.okrproject.application.team;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberService;
import kr.objet.okrproject.domain.team.service.fixture.TeamMemberCommandFixture;
import kr.objet.okrproject.domain.team.service.fixture.TeamMemberFixture;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.service.UserService;
import org.junit.jupiter.api.*;
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
class TeamMemberFacadeTest {

    private TeamMemberFacade sut;

    @Mock
    private ProjectMasterService projectMasterService;

    @Mock
    private TeamMemberService teamMemberService;

    @Mock
    private UserService userService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        sut = new TeamMemberFacade(
                projectMasterService,
                teamMemberService,
                userService
        );
    }

    @Test
    void 신규_팀원_등록_성공() throws Exception {
        //given
        TeamMemberCommand.InviteTeamMember command
                = TeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();
        TeamMember teamMember = TeamMemberFixture.createMember(user, ProjectRoleType.LEADER, projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        given(userService.findUsersByEmails(command.getUserEmails()))
                .willReturn(List.of(user));
        given(teamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)))
                .willReturn(List.of(teamMember));
        given(teamMemberService.checkUsersAndRegisterTeamMember(List.of(user), List.of(teamMember), projectMaster))
                .willReturn(mock(TeamMemberSavedInfo.class));
        doNothing().when(teamMemberService).checkIsUserProjectLeader(projectMaster.getTeamMember(), user);

        //when
        assertDoesNotThrow(() -> sut.inviteTeamMembers(command,user));

        //then
        then(projectMasterService).should(times(1))
                .validateProjectMasterWithUser(command.getProjectToken(), user);
        then(userService).should(times(1))
                .findUsersByEmails(command.getUserEmails());
        then(teamMemberService).should(times(1))
                .findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user));
        then(teamMemberService).should(times(1))
                .checkUsersAndRegisterTeamMember(List.of(user), List.of(teamMember), projectMaster);
        then(teamMemberService).should(times(1))
                .checkIsUserProjectLeader(projectMaster.getTeamMember(), user);

    }


    @Test
    void 신규_팀원_등록_실패_리더가아님() throws Exception {
        //given
        TeamMemberCommand.InviteTeamMember command
                = TeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();
        TeamMember teamMember = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER, projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        doThrow(new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN))
                .when(teamMemberService).checkIsUserProjectLeader(projectMaster.getTeamMember(), user);


        //when
        assertThrows(OkrApplicationException.class,() -> sut.inviteTeamMembers(command,user));
    }

    @Test
    void 신규_팀원_등록_실패_추가된_팀원이_없음() throws Exception {
        //given
        TeamMemberCommand.InviteTeamMember command
                = TeamMemberCommandFixture.createMember(1, 5);
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();
        TeamMember teamMember = TeamMemberFixture.createMember(user, ProjectRoleType.LEADER, projectMaster);

        given(projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user))
                .willReturn(projectMaster);
        given(userService.findUsersByEmails(command.getUserEmails()))
                .willReturn(List.of(user));
        given(teamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)))
                .willReturn(List.of(teamMember));
        given(teamMemberService.checkUsersAndRegisterTeamMember(List.of(user), List.of(teamMember), projectMaster))
                .willThrow(new OkrApplicationException(ErrorCode.NO_USERS_ADDED));
        doNothing().when(teamMemberService).checkIsUserProjectLeader(projectMaster.getTeamMember(), user);


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
        doNothing().when(teamMemberService).validateEmailWithProject(email, projectMaster.getId());
        doNothing().when(userService).validateUserWithEmail(email);


        //when
        String validatedEmail = assertDoesNotThrow(() -> sut.validateEmail(token, email, user));

        //then
        assertThat(validatedEmail).isEqualTo(email);
    }

    @Test
    void 팀원_초대_이메일_유효성검사_가입요청자_프로젝트_토큰_오류() throws Exception {
        //given
        String token = "projectToken";
        String email = "test@test.com";
        User user = UserFixture.create();

        doThrow(new OkrApplicationException(ErrorCode.INVALID_PROJECT_TOKEN))
                .when(projectMasterService).validateProjectMasterWithUser(token, user);

        //when
        OkrApplicationException exception = assertThrows(OkrApplicationException.class, () -> sut.validateEmail(token, email, user));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_PROJECT_TOKEN.getMessage());
    }

    @Test
    void 팀원_초대_이메일_유효성검사_가입불가_자기자신() throws Exception {
        //given
        String token = "projectToken";
        String email = "test@test.com";
        User user = UserFixture.create(1L, email);
        ProjectMaster projectMaster = ProjectMasterFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(token, user))
                .willReturn(projectMaster);


        //when
        OkrApplicationException exception = assertThrows(OkrApplicationException.class, () -> sut.validateEmail(token, email, user));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.NOT_AVAIL_INVITE_MYSELF.getMessage());
    }

    @Test
    void 팀원_초대_이메일_유효성검사_가입불가_이미_프로젝트에_초대된_유저() throws Exception {
        //given
        String token = "projectToken";
        String email = "test@test.com";
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(token, user))
                .willReturn(projectMaster);

        doThrow(new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER))
                .when(teamMemberService).validateEmailWithProject( email, projectMaster.getId());


        //when
        OkrApplicationException exception = assertThrows(OkrApplicationException.class, () -> sut.validateEmail(token, email, user));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
    }


    @Test
    void 팀원_초대_이메일_유효성검사_가입불가_어플리케이션에_가입된_유저X() throws Exception {
        //given
        String token = "projectToken";
        String email = "test@test.com";
        User user = UserFixture.create();
        ProjectMaster projectMaster = ProjectMasterFixture.create();

        given(projectMasterService.validateProjectMasterWithUser(token, user))
                .willReturn(projectMaster);
        doNothing().when(teamMemberService).validateEmailWithProject(email, projectMaster.getId());

        doThrow(new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL))
                .when(userService).validateUserWithEmail( email);


        //when
        OkrApplicationException exception = assertThrows(OkrApplicationException.class, () -> sut.validateEmail(token, email, user));

        //then
        assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_USER_EMAIL.getMessage());
    }
}