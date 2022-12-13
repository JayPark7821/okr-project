package kr.objet.okrproject.domain.user.service;

import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;

import java.util.List;

public interface UserService {
	UserInfo.AuthProcess getUserInfoFromIdToken(ProviderType providerType, String idToken);
	User store(User user);
	User findUserBy(String email);
	User loadUserByEmail(String email);
	List<User> findUsersByEmails(List<String> emails);

	void validateUserWithEmail(String email);
}
