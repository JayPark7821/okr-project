package kr.objet.okrproject.domain.user.service;

import java.util.Optional;

import kr.objet.okrproject.domain.user.User;

public interface UserReader {
	User getUserByUsername(String username);

	Optional<User> findUserByUserId(String userId);
}
