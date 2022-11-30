package kr.objet.okrproject.domain.user.service;

import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;

public interface UserService {
	UserInfo.Main getUserInfoFromIdToken(String providerType, String idToken);

	boolean isJoining(UserInfo.Main userInfo, String providerType);

	User loadUserByEmail(String email);
}
