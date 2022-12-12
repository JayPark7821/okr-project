package kr.objet.okrproject.domain.team.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberReader;
import kr.objet.okrproject.domain.team.service.TeamMemberService;
import kr.objet.okrproject.domain.team.service.TeamMemberStore;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberServiceImpl implements TeamMemberService {

	private final TeamMemberStore teamMemberStore;
	private final TeamMemberReader teamMemberReader;

	@Override
	public TeamMember registerProjectTeamMember(TeamMemberCommand.RegisterProjectLeader command) {
		TeamMember teamMember = command.toEntity();
		return teamMemberStore.store(teamMember);
	}

	@Override
	public void checkIsUserProjectLeader(List<TeamMember> teamMemberList, User user) {
		teamMemberList.stream()
			.filter(t -> t.getUser().getEmail().equals(user.getEmail())
				&& t.isTeamLeader())
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	@Override
	public List<TeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users) {
		return teamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, users);
	}

	@Override
	public TeamMemberSavedInfo checkUsersAndRegisterTeamMember(
		List<User> users,
		List<TeamMember> teamMembers,
		ProjectMaster projectMaster
	) {

		List<String> addedEmailList = new ArrayList<>();

		for (User user : users) {
			if (teamMembers.stream()
				.noneMatch(t -> t.getUser().getEmail().equals(user.getEmail()))) {
				TeamMember teamMember = TeamMember.builder()
					.projectMaster(projectMaster)
					.projectRoleType(ProjectRoleType.MEMBER)
					.isNew(true)
					.user(user)
					.build();
				teamMemberStore.store(teamMember);
				addedEmailList.add(user.getEmail());
			}
		}

		if (addedEmailList.size() == 0) {
			throw new OkrApplicationException(ErrorCode.NO_USERS_ADDED);
		}

		return teamMembers.size() == 1 ?
			new TeamMemberSavedInfo(addedEmailList, true) :
			new TeamMemberSavedInfo(addedEmailList, false);
	}

	@Override
	public void validateEmailWithProject(String email, Long projectId) {
		List<TeamMember> teamMemberList = teamMemberReader.findTeamMembersByProjectId(projectId);
		if (teamMemberList.stream()
			.anyMatch(t -> t.getUser().getEmail().equals(email))) {
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);
		}
	}
}
