package kr.objet.okrproject.domain.user.service;

import javax.servlet.http.HttpServletRequest;

import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;

public interface UserService {
	UserInfo.Main loginWithSocialIdToken(HttpServletRequest request, String providerType, String idToken);

	User loadUserByUsername(String username);
}
