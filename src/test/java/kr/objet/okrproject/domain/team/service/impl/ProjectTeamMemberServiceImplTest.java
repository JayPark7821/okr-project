package kr.objet.okrproject.domain.team.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberReader;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberStore;
import kr.objet.okrproject.domain.team.service.fixture.ProjectTeamMemberCommandFixture;

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
		sut = new ProjectTeamMemberServiceImpl(projectTeamMemberStore, projectTeamMemberReader);
	}

	@Test
	void 팀원_등록_성공 () throws Exception {
	    //given
		ProjectTeamMemberCommand.RegisterProjectTeamMember command = ProjectTeamMemberCommandFixture.create();
		ProjectTeamMember projectTeamMember = command.toEntity();

		given(projectTeamMemberStore.store(any())).willReturn(projectTeamMember);

		//when
		ProjectTeamMember savedProjectTeamMember = assertDoesNotThrow(() -> sut.registerProjectTeamMember(command));
		//then
		assertEquals(command.getProjectMaster().getName(), savedProjectTeamMember.getProjectMaster().getName());
	}

}