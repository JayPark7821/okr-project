package kr.objet.okrproject.domain.team.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import kr.objet.okrproject.application.user.fixture.UserFixture;
import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
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

import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberReader;
import kr.objet.okrproject.domain.team.service.TeamMemberStore;
import kr.objet.okrproject.domain.team.service.fixture.TeamMemberCommandFixture;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class TeamMemberServiceImplTest {

	private TeamMemberServiceImpl sut;

	@Mock
	private TeamMemberStore teamMemberStore;

	@Mock
	private TeamMemberReader teamMemberReader;


	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new TeamMemberServiceImpl(teamMemberStore, teamMemberReader);
	}

	@Test
	void 팀원_등록_성공() throws Exception {
		//given
		TeamMemberCommand.RegisterProjectLeader command = TeamMemberCommandFixture.createLeader();
		TeamMember teamMember = command.toEntity();

		given(teamMemberStore.store(any())).willReturn(teamMember);

		//when
		TeamMember savedTeamMember = assertDoesNotThrow(() -> sut.registerProjectTeamMember(command));
		//then
		assertEquals(command.getProjectMaster().getName(), savedTeamMember.getProjectMaster().getName());
	}

	@Test
	void 팀원이_리더()throws Exception {
		//given
		User user = UserFixture.create(1L, "test@test.com");
		TeamMember leader = TeamMemberFixture.createMember(user, ProjectRoleType.LEADER);

		//when
		assertDoesNotThrow(() -> sut.checkIsUserProjectLeader(List.of(leader), user));
	}

	@Test
	void 팀원이_리더가_아님()throws Exception {
		//given
		User user = UserFixture.create(1L, "test@test.com");
		TeamMember member = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER);

		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
				() -> sut.checkIsUserProjectLeader(List.of(member), user));

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.USER_IS_NOT_LEADER.getMessage());

	}


	@Test
	void Users로_팀원_조회()throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User user = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER,projectMaster);

		given(teamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)))
				.willReturn(List.of(member));

		//when
		List<TeamMember> teamMembers = assertDoesNotThrow(() -> sut.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)));

		//then
		assertThat(teamMembers).contains(member);
	}


	@Test
	void 프로젝트에_팀원추가_성공()throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create();
		User notTeamMember = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER,projectMaster);

		//when
		TeamMemberSavedInfo teamMemberSavedInfo =
				assertDoesNotThrow(() -> sut.checkUsersAndRegisterTeamMember(
					List.of(alreadyTeamMember, notTeamMember),
					List.of(member),
					projectMaster
				)
		);

		// TODO : @@@@@@@@@@@@================> return 값 null

		//then
		assertThat(teamMemberSavedInfo.getAddedEmailList()).contains(notTeamMember.getEmail());
		assertThat(teamMemberSavedInfo.getMessage()).isEqualTo(Notifications.PROJECT_TYPE_CHANGE.getMsg());

	}

	@Test
	void 프로젝트에_추가된_팀원_없음()throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER,projectMaster);

		//when

		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.checkUsersAndRegisterTeamMember(
					List.of(alreadyTeamMember),
					List.of(member),
					projectMaster
			)
		);

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.NO_USERS_ADDED.getMessage());
	}

	@Test
	void 프로젝트에_이미_가입되어있는_이메일()throws Exception {
		//given
		String email = "test@test.com";
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create(1L,email);
		TeamMember member = TeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER,projectMaster);

		given(teamMemberReader.findTeamMembersByProjectId(projectMaster.getId()))
				.willReturn(List.of(member));

		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.validateEmailWithProject(
					email,
					projectMaster.getId()
			)
		);

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.USER_ALREADY_PROJECT_MEMBER.getMessage());
	}


	@Test
	void 이메일_검증_성공()throws Exception {
		//given
		String email = "test@test.com";
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER,projectMaster);

		given(teamMemberReader.findTeamMembersByProjectId(projectMaster.getId()))
				.willReturn(List.of(member));

		//when
		assertDoesNotThrow(
			() -> sut.validateEmailWithProject(
				email,
				projectMaster.getId()
			)
		);
	}



}