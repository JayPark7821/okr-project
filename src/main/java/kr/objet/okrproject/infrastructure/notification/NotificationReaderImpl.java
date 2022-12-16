package kr.objet.okrproject.infrastructure.notification;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.notification.service.NotificationReader;
import kr.objet.okrproject.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationReaderImpl implements NotificationReader {

	private final NotificationRepository notificationRepository;

	@Override
	public List<Notification> findNotificationsByUser(User user) {
		return notificationRepository.findNotificationsByUser(user);
	}
}
