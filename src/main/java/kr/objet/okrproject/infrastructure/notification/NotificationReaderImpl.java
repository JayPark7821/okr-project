package kr.objet.okrproject.infrastructure.notification;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.domain.notification.service.NotificationReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationReaderImpl implements NotificationReader {

	private final NotificationRepository notificationRepository;

}
