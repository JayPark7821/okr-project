package kr.objet.okrproject.infrastructure.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.user.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("select n " +
		"from Notification n " +
		"join n.user u " +
		"where u.email = :email " +
		"and n.status <> kr.objet.okrproject.domain.notification.NotificationCheckType.DELETED ")
	List<Notification> findAllByEmail(@Param("email") String email);

	@Query("select n " +
		"from Notification n " +
		"where n.user =:user " +
		"and n.status <> kr.objet.okrproject.domain.notification.NotificationCheckType.DELETED ")
	List<Notification> findNotificationsByUser(@Param("user") User user);

	Optional<Notification> findByNotificationToken(String notiToken);

	Optional<Notification> findByUserAndNotificationToken(User user, String token);
}
