package kr.objet.okrproject.infrastructure.notification;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.notification.Notification;
import kr.objet.okrproject.domain.user.User;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
	List<Notification> findAllByUser(User user);

	@Query("select n " +
		"from Notification n " +
		"join n.user u " +
		"where u.email = :email")
	List<Notification> findAllByEmail(@Param("email") String email);

	List<Notification> findNotificationsByUser(User user);

	Optional<Notification> findByNotificationToken(String notiToken);
}
