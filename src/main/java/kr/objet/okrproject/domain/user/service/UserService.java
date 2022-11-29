package kr.objet.okrproject.domain.user.service;

import javax.servlet.http.HttpServletRequest;

import kr.objet.okrproject.interfaces.user.UserDto;
import kr.objet.okrproject.domain.user.User;

public interface UserService {
	UserDto.LoginResponse loginWithSocialIdToken(HttpServletRequest request, String providerType, String idToken);

	User loadUserByUsername(String username);
}
