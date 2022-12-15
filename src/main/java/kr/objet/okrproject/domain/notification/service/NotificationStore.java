package kr.objet.okrproject.domain.notification.service;

import kr.objet.okrproject.domain.notification.Notification;

public interface NotificationStore {
	void store(Notification entity);
}
