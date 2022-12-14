package kr.objet.okrproject.infrastructure.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import kr.objet.okrproject.domain.user.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByEmail(String email);

	Optional<User> findUserByUserId(String userId);

	@Query("select u " +
		"from User u " +
		"where u.email in :emails")
	List<User> findUsersByEmails(@Param("emails") List<String> emails);
}
