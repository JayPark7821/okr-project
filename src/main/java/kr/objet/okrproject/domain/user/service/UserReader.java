package kr.objet.okrproject.domain.user.service;

import kr.objet.okrproject.domain.user.User;

public interface UserReader {
	User getUserByUsername(String username);

	User getUserByUserId(String userId);
}
