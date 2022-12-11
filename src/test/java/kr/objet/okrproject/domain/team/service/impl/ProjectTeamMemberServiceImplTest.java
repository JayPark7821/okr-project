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
import kr.objet.okrproject.domain.team.ProjectTeamMemberSavedInfo;
import kr.objet.okrproject.domain.team.service.fixture.ProjectTeamMemberFixture;
import kr.objet.okrproject.domain.user.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberReader;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberStore;
import kr.objet.okrproject.domain.team.service.fixture.ProjectTeamMemberCommandFixture;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class ProjectTeamMemberServiceImplTest {

	private ProjectTeamMemberServiceImpl sut;

	@Mock
	private ProjectTeamMemberStore projectTeamMemberStore;

	@Mock
	private ProjectTeamMemberReader projectTeamMemberReader;


	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new ProjectTeamMemberServiceImpl(projectTeamMemberStore,projectTeamMemberReader);
	}

	@Test
	void 팀원_등록_성공() throws Exception {
		//given
		ProjectTeamMemberCommand.RegisterProjectLeader command = ProjectTeamMemberCommandFixture.createLeader();
		ProjectTeamMember projectTeamMember = command.toEntity();

		given(projectTeamMemberStore.store(any())).willReturn(projectTeamMember);

		//when
		ProjectTeamMember savedProjectTeamMember = assertDoesNotThrow(() -> sut.registerProjectTeamMember(command));
		//then
		assertEquals(command.getProjectMaster().getName(), savedProjectTeamMember.getProjectMaster().getName());
	}

	@Test
	void 팀원이_리더()throws Exception {
		//given
		User user = UserFixture.create(1L, "test@test.com");
		ProjectTeamMember leader = ProjectTeamMemberFixture.createMember(user, ProjectRoleType.LEADER);

		//when
		assertDoesNotThrow(() -> sut.checkIsUserProjectLeader(List.of(leader), user));
	}

	@Test
	void 팀원이_리더가_아님()throws Exception {
		//given
		User user = UserFixture.create(1L, "test@test.com");
		ProjectTeamMember member = ProjectTeamMemberFixture.createMember(user, ProjectRoleType.MEMBER);

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
		ProjectTeamMember member = ProjectTeamMemberFixture.createMember(user, ProjectRoleType.MEMBER,projectMaster);

		given(projectTeamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)))
				.willReturn(List.of(member));

		//when
		List<ProjectTeamMember> projectTeamMembers = assertDoesNotThrow(() -> sut.findTeamMembersByProjectMasterAndUsers(projectMaster, List.of(user)));

		//then
		assertThat(projectTeamMembers).contains(member);
	}


	@Test
	void 프로젝트에_팀원추가_성공()throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create();
		User notTeamMember = UserFixture.create();
		ProjectTeamMember member = ProjectTeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER,projectMaster);

		//when
		ProjectTeamMemberSavedInfo projectTeamMemberSavedInfo =
				assertDoesNotThrow(() -> sut.checkUsersAndRegisterTeamMember(
					List.of(alreadyTeamMember, notTeamMember),
					List.of(member),
					projectMaster
				)
		);

		// TODO : @@@@@@@@@@@@================> return 값 null

		//then
		assertThat(projectTeamMemberSavedInfo.getAddedEmailList()).contains(notTeamMember.getEmail());
		assertThat(projectTeamMemberSavedInfo.getMessage()).isEqualTo(Notifications.PROJECT_TYPE_CHANGE.getMsg());

	}

	@Test
	void 프로젝트에_추가된_팀원_없음()throws Exception {
		//given
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create();
		ProjectTeamMember member = ProjectTeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER,projectMaster);

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



}