package kr.objet.okrproject.domain.notification.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.notification.NotificationInfo;
import kr.objet.okrproject.domain.notification.service.NotificationCommand;
import kr.objet.okrproject.domain.notification.service.NotificationReader;
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.notification.service.NotificationStore;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationStore notificationStore;
	private final NotificationReader notificationReader;

	@Override
	public void pushNotification(List<NotificationCommand.send> commands) {
		commands.forEach(c -> {
			notificationStore.store(c.toEntity());
		});

	}

	@Override
	public List<NotificationInfo.Response> findNotificationsByUser(User user) {
		List<Notification> notifications = notificationReader.findNotificationsByUser(user);
		return notifications.stream()
			.map(NotificationInfo.Response::new)
			.collect(Collectors.toList());
	}
}
