package kr.objet.okrproject.domain.notification.service;

import java.util.List;

import kr.objet.okrproject.domain.notification.NotificationInfo;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;

public interface NotificationService {

	void pushNotifications(List<NotificationCommand.send> commands);

	void pushNotification(NotificationCommand.send command);

	List<NotificationInfo.Response> findNotificationsByUser(User user);

	void updateNotificationStatus(User user, String token);

	void sendIniDoneNoti(List<TeamMember> teamMember, User user, String InitiativeName);

	void sendProjectProgressNoti(ProjectMaster projectMaster, Double beforeProgress, Double afterProgress);

}
