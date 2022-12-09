package kr.objet.okrproject.domain.team.service.impl;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamMemberServiceImpl implements ProjectTeamMemberService {

	private final ProjectTeamMemberStore projectTeamMemberStore;
	// private final ProjectTeamMemberReader projectTeamMemberReader;

	@Override
	public ProjectTeamMember registerProjectTeamMember(ProjectTeamMemberCommand.RegisterProjectLeader command) {
		ProjectTeamMember projectTeamMember = command.toEntity();
		return projectTeamMemberStore.store(projectTeamMember);
	}
}
