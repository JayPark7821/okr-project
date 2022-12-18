package kr.objet.okrproject.domain.notification.service;

import kr.objet.okrproject.domain.notification.NotificationInfo;
import kr.objet.okrproject.domain.project.ProjectMaster;
import kr.objet.okrproject.domain.team.TeamMember;
import kr.objet.okrproject.domain.user.User;

import java.util.List;

public interface NotificationService {

	void pushNotification(List<NotificationCommand.send> commands);

	List<NotificationInfo.Response> findNotificationsByUser(User user);

	void updateNotificationStatus(User user, String token);

    void sendIniDoneNoti(List<TeamMember> teamMember, User user, String InitiativeName);

	void sendProjectProgressNoti(ProjectMaster projectMaster, Double beforeProgress, Double afterProgress);

}
