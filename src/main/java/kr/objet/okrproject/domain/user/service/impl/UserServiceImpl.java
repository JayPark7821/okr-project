package kr.objet.okrproject.domain.user.service.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.domain.user.ProviderType;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.auth.TokenVerifyProcessor;
import kr.objet.okrproject.domain.user.service.UserReader;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.interfaces.user.UserDto;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;
	private final TokenVerifyProcessor tokenVerifyProcessor;

	@Override
	public UserDto.LoginResponse loginWithSocialIdToken(HttpServletRequest request, String provider,
		String idToken) {
		ProviderType providerType = ProviderType.of(provider);
		OAuth2UserInfo oAuth2UserInfo = tokenVerifyProcessor.verifyIdToken(providerType, idToken);

		return null;
	}

	public User loadUserByUsername(String username) {
		return userReader.getUserBy(username);
	}
}
