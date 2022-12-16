package kr.objet.okrproject.domain.notification.service;

import java.util.List;
import java.util.Optional;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.user.User;

public interface NotificationReader {
	List<Notification> findNotificationsByUser(User user);

	Optional<Notification> findByUserAndNotificationToken(User user, String token);

}
