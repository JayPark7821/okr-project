package kr.objet.okrproject.domain.notification.service;

import java.util.List;

public interface NotificationService {

	void pushNotification(List<NotificationCommand.send> commands);
}
