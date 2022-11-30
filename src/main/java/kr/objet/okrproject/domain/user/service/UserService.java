package kr.objet.okrproject.domain.user.service;

import kr.objet.okrproject.domain.user.User;

public interface UserService {
	User getUserInfoFromIdToken(String providerType, String idToken);

	boolean isJoining(User user, String providerType);

	User store(User user);

	User findUserInfoBy(String email);
	User loadUserByEmail(String email);
}
