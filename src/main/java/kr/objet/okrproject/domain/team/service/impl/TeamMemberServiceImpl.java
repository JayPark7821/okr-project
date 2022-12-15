package kr.objet.okrproject.domain.team.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
	public void validateEmailWithProject(String email, Long projectId) {
		List<TeamMember> teamMemberList = teamMemberReader.findTeamMembersByProjectId(projectId);
		if (teamMemberList.stream()
			.anyMatch(t -> t.getUser().getEmail().equals(email))) {
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);
		}
	}

	@Override
	public TeamMemberSavedInfo inviteTeamMembers(ProjectMaster projectMaster, User user, List<User> users) {

		checkIfUserProjectLeader(projectMaster.getTeamMember(), user);

		List<TeamMember> beforeTeamMembers =
			teamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, users);

		List<User> canBeSavedAsMemberList = checkUsersWhetherTeamMemberOrNot(users, beforeTeamMembers);

		if (canBeSavedAsMemberList.size() > 0) {
			registerUsersAsTeamMember(canBeSavedAsMemberList, projectMaster);

			List<String> invitedMembersEmail =
				canBeSavedAsMemberList.stream()
					.map(User::getEmail)
					.collect(Collectors.toList());

			return beforeTeamMembers.size() == 1 ?
				new TeamMemberSavedInfo(invitedMembersEmail, true) :
				new TeamMemberSavedInfo(invitedMembersEmail, false);
		} else {
			throw new OkrApplicationException(ErrorCode.NO_USERS_ADDED);
		}
	}

	private List<User> checkUsersWhetherTeamMemberOrNot(List<User> users, List<TeamMember> teamMembers) {
		return users.stream()
			.filter(u -> teamMembers.stream().noneMatch(t -> t.getUser().getEmail().equals(u.getEmail())))
			.collect(Collectors.toList());
	}

	private void checkIfUserProjectLeader(List<TeamMember> teamMemberList, User user) {
		teamMemberList.stream()
			.filter(t -> t.getUser().getEmail().equals(user.getEmail())
				&& t.isTeamLeader())
			.findAny()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	private void registerUsersAsTeamMember(List<User> users, ProjectMaster projectMaster) {
		List<String> addedEmailList = new ArrayList<>();
		users.stream()
			.map(u -> new TeamMember(u, projectMaster, ProjectRoleType.MEMBER, true))
			.forEach(teamMemberStore::store);
	}

}
