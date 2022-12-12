package kr.objet.okrproject.application.team;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.service.TeamMemberCommand;
import kr.objet.okrproject.domain.team.service.TeamMemberService;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.interfaces.team.TeamMemberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeamMemberFacade {

	private final ProjectMasterService projectMasterService;
	private final TeamMemberService teamMemberService;
	private final UserService userService;

	public TeamMemberDto.saveResponse inviteTeamMembers(TeamMemberCommand.InviteTeamMember command, User user) {
		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(
			command.getProjectToken(),
			user
		);
		teamMemberService.checkIsUserProjectLeader(projectMaster.getTeamMember(), user);

		List<User> users = userService.findUsersByEmails(command.getUserEmails());
		List<TeamMember> teamMembers = teamMemberService.findTeamMembersByProjectMasterAndUsers(projectMaster, users);

		return teamMemberService.checkUsersAndRegisterTeamMember(users, teamMembers, projectMaster).toDto();
	}

	public String validateEmail(String projectToken, String email, User user) {
		ProjectMaster projectMaster = projectMasterService.validateProjectMasterWithUser(projectToken, user);

		if (user.getEmail().equals(email)) {
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);
		}

		teamMemberService.validateEmailWithProject(email, projectMaster.getId());
		userService.validateUserWithEmail(email);
		return email;
	}
}
