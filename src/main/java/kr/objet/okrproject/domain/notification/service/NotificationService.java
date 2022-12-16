package kr.objet.okrproject.domain.notification.service;

import java.util.List;

import kr.objet.okrproject.domain.notification.NotificationInfo;
import kr.objet.okrproject.domain.user.User;

public interface NotificationService {

	void pushNotification(List<NotificationCommand.send> commands);

	List<NotificationInfo.Response> findNotificationsByUser(User user);
}
