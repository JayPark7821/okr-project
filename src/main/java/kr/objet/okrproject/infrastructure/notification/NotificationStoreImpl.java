package kr.objet.okrproject.infrastructure.notification;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.notification.service.NotificationStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationStoreImpl implements NotificationStore {

	private final NotificationRepository notificationRepository;

	@Override
	public void store(Notification entity) {
		notificationRepository.save(entity);
	}
}
