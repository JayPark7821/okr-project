package kr.objet.okrproject.application.team;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.ProjectTeamMember;
import kr.objet.okrproject.domain.team.service.ProjectTeamMemberService;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.interfaces.team.ProjectTeamMemberDto;
import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.team.service.ProjectTeamMemberCommand;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectTeamMemberFacade {

	private final ProjectMasterService projectMasterService;
	private final ProjectTeamMemberService projectTeamMemberService;
	private final UserService userService;

	public ProjectTeamMemberDto.saveResponse inviteTeamMembers(ProjectTeamMemberCommand.InviteProjectTeamMember command, User user) {
		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(command.getProjectToken(), user);
		projectTeamMemberService.checkIsUserProjectLeader(projectMaster.getProjectTeamMember(), user);

		List<User> users = userService.findUsersByEmails(command.getUserEmails());
	 	List<ProjectTeamMember> teamMembers = projectTeamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, users);

		return projectTeamMemberService.checkUsersAndRegisterTeamMember(users, teamMembers, projectMaster).toDto();
	}

	public String validateEmail(String projectToken, String email, User user) {
		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(projectToken, user);

		if (user.getEmail().equals(email)) {
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);
		}

		projectTeamMemberService.validateEmailWithProject(email,projectMaster.getId());
		userService.validateUserWithEmail(email);
		return email;
	}
}
