package kr.objet.okrproject.application.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.notification.NotificationInfo;
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationFacade {

	private final NotificationService notificationService;

	public List<NotificationInfo.Response> getNotifications(User user) {
		return notificationService.findNotificationsByUser(user);
	}

	public void checkNotification(User user, String token) {
		notificationService.checkNotification(user, token);
	}

	public void deleteNotification(User user, String token) {
		notificationService.deleteNotification(user, token);
	}
}
