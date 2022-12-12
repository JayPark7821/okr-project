package kr.objet.okrproject.domain.keyresult.service.impl;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

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
import kr.objet.okrproject.domain.keyresult.service.KeyResultReader;
import kr.objet.okrproject.domain.keyresult.service.KeyResultStore;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultCommandFixture;
import kr.objet.okrproject.domain.keyresult.service.fixture.KeyResultFixture;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.fixture.ProjectMasterFixture;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.service.fixture.TeamMemberFixture;
import kr.objet.okrproject.domain.user.User;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class KeyResultServiceImplTest {

	private KeyResultServiceImpl sut;

	@Mock
	private KeyResultStore keyResultStore;
	@Mock
	private KeyResultReader keyResultReader;

	@BeforeEach
	void init() {
		MockitoAnnotations.openMocks(this);
		sut = new KeyResultServiceImpl(keyResultStore, keyResultReader);
	}

	@Test
	void 키리절트_등록_성공() throws Exception {
		//given
		KeyResultCommand.RegisterKeyResultWithProject command = KeyResultCommandFixture.create();
		KeyResult keyResult = command.toEntity();
		given(keyResultStore.store(any())).willReturn(keyResult);

		//when
		KeyResult savedKeyResult = assertDoesNotThrow(() -> sut.registerKeyResult(command));

		//then
		assertNotNull(savedKeyResult);
		assertEquals(keyResult.getName(), savedKeyResult.getName());
	}

	@Test
	void keyResult_벨리데이션_성공() throws Exception {
		//given
		User user = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER);
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		projectMaster.addTeamMember(member);
		KeyResult keyResult = KeyResultFixture.create(projectMaster);

		given(keyResultReader.findByKeyResultTokenAndEmail(keyResult.getKeyResultToken(), user))
			.willReturn(Optional.of(keyResult));

		//when
		KeyResult savedKeyResult = assertDoesNotThrow(
			() -> sut.validateKeyResultWithUser(keyResult.getKeyResultToken(), user));

		//then
		assertThat(keyResult.getKeyResultToken()).isEqualTo(savedKeyResult.getKeyResultToken());
	}

	@Test
	void keyResult_벨리데이션_실패() throws Exception {
		//given
		User user = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER);
		ProjectMaster projectMaster = ProjectMasterFixture.create(member);
		KeyResult keyResult = KeyResultFixture.create(projectMaster);

		given(keyResultReader.findByKeyResultTokenAndEmail(keyResult.getKeyResultToken(), user))
			.willReturn(Optional.empty());

		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.validateKeyResultWithUser(keyResult.getKeyResultToken(), user));

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_KEYRESULT_TOKEN.getMessage());
	}

	@Test
	void keyResult_벨리데이션_실패_팀맴버_2명_조회() throws Exception {
		//given
		User user = UserFixture.create();
		TeamMember member = TeamMemberFixture.createMember(user, ProjectRoleType.MEMBER);
		User user1 = UserFixture.create();
		TeamMember member1 = TeamMemberFixture.createMember(user1, ProjectRoleType.MEMBER);
		ProjectMaster projectMaster = ProjectMasterFixture.create();
		projectMaster.addTeamMember(member);
		projectMaster.addTeamMember(member1);
		KeyResult keyResult = KeyResultFixture.create(projectMaster);

		given(keyResultReader.findByKeyResultTokenAndEmail(keyResult.getKeyResultToken(), user))
			.willReturn(Optional.of(keyResult));

		//when
		OkrApplicationException exception = assertThrows(OkrApplicationException.class,
			() -> sut.validateKeyResultWithUser(keyResult.getKeyResultToken(), user));

		//then
		assertThat(exception.getMessage()).isEqualTo(ErrorCode.INVALID_USER_INFO.getMessage());
	}
}