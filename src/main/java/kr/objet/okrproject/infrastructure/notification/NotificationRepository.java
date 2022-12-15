package kr.objet.okrproject.infrastructure.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import kr.objet.okrproject.domain.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
