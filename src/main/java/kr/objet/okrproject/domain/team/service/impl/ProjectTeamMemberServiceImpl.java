package kr.objet.okrproject.domain.team.service.impl;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberReader;
import kr.objet.okrproject.domain.user.User;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamMemberServiceImpl implements ProjectTeamMemberService {

	private final ProjectTeamMemberStore projectTeamMemberStore;
	private final ProjectTeamMemberReader projectTeamMemberReader;

	@Override
	public ProjectTeamMember registerProjectTeamMember(ProjectTeamMemberCommand.RegisterProjectLeader command) {
		ProjectTeamMember projectTeamMember = command.toEntity();
		return projectTeamMemberStore.store(projectTeamMember);
	}

	@Override
	public void checkIsUserProjectLeader(List<ProjectTeamMember> teamMemberList, User user) {
		teamMemberList.stream()
				.filter(t -> t.getUser().equals(user)
						&& t.isTeamLeader())
				.findAny()
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	@Override
	public List<ProjectTeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users) {
		return projectTeamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, users);
	}
}
