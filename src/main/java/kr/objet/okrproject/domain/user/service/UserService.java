package kr.objet.okrproject.domain.user.service;

import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;

public interface UserService {
	OAuth2UserInfo getUserInfoFromIdToken(ProviderType providerType, String idToken);
	boolean isJoining(User user, ProviderType providerType);
	User store(User user);
	User findUserBy(String email);
	User loadUserByEmail(String email);
}
