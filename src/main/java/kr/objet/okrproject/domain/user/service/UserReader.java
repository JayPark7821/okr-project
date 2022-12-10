package kr.objet.okrproject.domain.user.service;

import java.util.List;
import java.util.Optional;

import kr.objet.okrproject.domain.user.User;

public interface UserReader {
	Optional<User> findUserByEmail(String email);

	Optional<User> findUserByUserId(String userId);

    List<User> findUsersByEmails(List<String> emails);
}
