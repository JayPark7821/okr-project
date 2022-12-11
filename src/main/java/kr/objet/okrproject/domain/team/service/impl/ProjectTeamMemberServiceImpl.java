package kr.objet.okrproject.domain.team.service.impl;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.ProjectRoleType;
import kr.objet.okrproject.domain.team.ProjectTeamMemberSavedInfo;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberReader;
import kr.objet.okrproject.domain.user.User;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
				.filter(t -> t.getUser().getEmail().equals(user.getEmail())
						&& t.isTeamLeader())
				.findAny()
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.USER_IS_NOT_LEADER));
	}

	@Override
	public List<ProjectTeamMember> findTeamMembersByProjectMasterAndUsers(ProjectMaster projectMaster, List<User> users) {
		return projectTeamMemberReader.findTeamMembersByProjectMasterAndUsers(projectMaster, users);
	}

	@Override
	public ProjectTeamMemberSavedInfo checkUsersAndRegisterTeamMember(
			List<User> users,
			List<ProjectTeamMember> teamMembers,
			ProjectMaster projectMaster
	) {

		List<String> addedEmailList = new ArrayList<>();

		for (User user : users) {
			if (teamMembers.stream().noneMatch(t -> t.getUser().getEmail().equals(user.getEmail()))) {
				ProjectTeamMember projectTeamMember = ProjectTeamMember.builder()
						.projectMaster(projectMaster)
						.projectRoleType(ProjectRoleType.MEMBER)
						.isNew(true)
						.user(user)
						.build();
				projectTeamMemberStore.store(projectTeamMember);
				addedEmailList.add(user.getEmail());
			}
		}

		if (addedEmailList.size() == 0) {
			throw new OkrApplicationException(ErrorCode.NO_USERS_ADDED);
		}

		return teamMembers.size() == 1 ?
				new ProjectTeamMemberSavedInfo(addedEmailList, true):
				new ProjectTeamMemberSavedInfo(addedEmailList, false);
	}

	@Override
	public void validateEmailWithProject(String email, Long projectId) {
		List<ProjectTeamMember> teamMemberList = projectTeamMemberReader.findTeamMembersByProjectId(projectId);
		if (teamMemberList.stream()
				.anyMatch(t -> t.getUser().getEmail().equals(email))) {
			throw new OkrApplicationException(ErrorCode.USER_ALREADY_PROJECT_MEMBER);
		}
	}
}
