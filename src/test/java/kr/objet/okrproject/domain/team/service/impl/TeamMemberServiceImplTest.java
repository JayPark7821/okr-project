package kr.objet.okrproject.domain.team.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
import kr.objet.okrproject.domain.team.service.TeamMemberReader;
import kr.objet.okrproject.domain.team.service.TeamMemberStore;
import kr.objet.okrproject.domain.team.service.fixture.TeamMemberFixture;
import kr.objet.okrproject.domain.user.User;

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
		User leader = UserFixture.create(1L, "leader@mail.com");
		List<User> userList = generateUserList(2, 5);

		ProjectMaster projectMaster = ProjectMasterFixture.create(
			TeamMemberFixture.createMember(leader, ProjectRoleType.LEADER)
		);

		given(teamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, userList))
			.willReturn(new ArrayList<TeamMember>());
		given(teamMemberStore.store(any())).willReturn(mock(TeamMember.class));

		//when
		TeamMemberSavedInfo teamMemberSavedInfo = assertDoesNotThrow(
			() -> sut.inviteTeamMembers(projectMaster, leader, userList)
		);

		//then
		assertThat(teamMemberSavedInfo.getAddedEmailList())
			.contains("test2@mail.com", "test3@mail.com", "test4@mail.com");
	}

	@Test
	void 팀원_등록_실패_요청자가_리더가_아님() throws Exception {
		//given
		User notLeader = UserFixture.create(1L, "member@mail.com");
		List<User> userList = generateUserList(2, 5);

		ProjectMaster projectMaster = ProjectMasterFixture.create(
			TeamMemberFixture.createMember(notLeader, ProjectRoleType.MEMBER)
		);

		//when
		assertThatThrownBy(() -> sut.inviteTeamMembers(projectMaster, notLeader, userList))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_IS_NOT_LEADER);

	}

	@Test
	void 팀원_등록_실패_팀원으로_추가된_유저가_없음() throws Exception {
		//given
		User leader = UserFixture.create(1L, "leader@mail.com");
		TeamMember leaderMember = TeamMemberFixture.createMember(leader, ProjectRoleType.LEADER);

		List<User> userList = generateUserList(2, 5);
		List<TeamMember> memberList = generateTeamMeberList(userList);

		ProjectMaster projectMaster = ProjectMasterFixture.create(memberList);
		projectMaster.addTeamMember(leaderMember);

		given(teamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, userList))
			.willReturn(memberList);

		//when
		assertThatThrownBy(() -> sut.inviteTeamMembers(projectMaster, leader, userList))
			.isExactlyInstanceOf(OkrApplicationException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.NO_USERS_ADDED);

	}

	@Test
	void 프로젝트에_이미_가입되어있는_이메일() throws Exception {
		//given
		String email = "test@test.com";
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create(1L, email);
		TeamMember member = TeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER, projectMaster);

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
	void 이메일_검증_성공() throws Exception {
		//given
		String email = "test@test.com";
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		User alreadyTeamMember = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(alreadyTeamMember, ProjectRoleType.MEMBER, projectMaster);

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

	private static List<User> generateUserList(int min, int max) {
		List<User> userList =
			IntStream.range(min, max)
				.mapToObj(i -> UserFixture.create(Integer.toUnsignedLong(i), "test" + i + "@mail.com"))
				.collect(Collectors.toList());
		return userList;
	}

	private static List<TeamMember> generateTeamMeberList(List<User> userList) {
		return userList.stream()
			.map(u -> TeamMemberFixture.createMember(u, ProjectRoleType.MEMBER))
			.collect(Collectors.toList());
	}

}