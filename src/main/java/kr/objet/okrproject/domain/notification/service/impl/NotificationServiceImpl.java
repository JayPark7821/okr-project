package kr.objet.okrproject.domain.notification.service.impl;

import kr.objet.okrproject.domain.notification.service.NotificationCommand;
import kr.objet.okrproject.domain.notification.service.NotificationService;
import kr.objet.okrproject.domain.notification.service.NotificationStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationStore notificationStore;

	@Override
	public void pushNotification(List<NotificationCommand.send> commands) {
		commands.forEach(c -> {
			notificationStore.store(c.toEntity());
		});

	}
}
