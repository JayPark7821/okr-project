package kr.objet.okrproject.application.team;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.notification.Notifications;
import kr.objet.okrproject.domain.notification.service.NotificationCommand;
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.project.service.ProjectMasterService;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.team.TeamMemberSavedInfo;
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
	private final NotificationService notificationService;

	public TeamMemberDto.saveResponse inviteTeamMembers(TeamMemberCommand.InviteTeamMember command, User user) {
		ProjectMaster projectMaster = projectMasterService.validateUserWithProjectMasterToken(
			command.getProjectToken(),
			user
		);

		List<User> users = userService.findUsersByEmails(command.getUserEmails());

		TeamMemberSavedInfo teamMemberSavedInfo =
			teamMemberService.inviteTeamMembers(projectMaster, user, users);

		List<TeamMember> notifyUser = teamMemberService.findTeamMembersByEmailsNotIn(
			teamMemberSavedInfo.getAddedEmailList(), projectMaster);

		pushNotificationsToTeamMembers(projectMaster, teamMemberSavedInfo, notifyUser);

		return teamMemberSavedInfo.toDto();
	}

	public String validateEmail(String projectToken, String email, User user) {
		ProjectMaster projectMaster = projectMasterService.validateUserWithProjectMasterToken(projectToken, user);

		if (user.getEmail().equals(email)) {
			throw new OkrApplicationException(ErrorCode.NOT_AVAIL_INVITE_MYSELF);
		}

		teamMemberService.validateEmailWithProject(email, projectMaster.getId());
		userService.validateUserWithEmail(email);
		return email;
	}

	private void pushNotificationsToTeamMembers(ProjectMaster projectMaster, TeamMemberSavedInfo teamMemberSavedInfo,
		List<TeamMember> notifyUser) {
		List<NotificationCommand.send> notifications = teamMemberSavedInfo.getAddedEmailList()
			.stream()
			.flatMap(e -> notifyUser.stream()
				.map(u -> new NotificationCommand.send(u.getUser(), Notifications.NEW_TEAM_MATE, e,
					projectMaster.getName())))
			.collect(Collectors.toList());

		notificationService.pushNotifications(notifications);
	}
}
