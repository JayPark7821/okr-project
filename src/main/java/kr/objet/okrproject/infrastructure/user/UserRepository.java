package kr.objet.okrproject.infrastructure.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.objet.okrproject.domain.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findUserByUsername(String username);

	Optional<User> findUserByUserId(String userId);
}
