package kr.objet.okrproject.domain.notification.service;

import java.util.List;

import kr.objet.okrproject.domain.notification.NotificationCommand;

public interface NotificationService {

	void pushNotification(List<NotificationCommand.send> commands);
}
