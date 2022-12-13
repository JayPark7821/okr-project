package kr.objet.okrproject.domain.user.service.impl;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.UserInfo;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.auth.TokenVerifyProcessor;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.service.UserReader;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.domain.user.service.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;
	private final UserStore userStore;
	private final TokenVerifyProcessor tokenVerifyProcessor;

	@Override
	public UserInfo.AuthProcess getUserInfoFromIdToken(ProviderType providerType, String idToken) {
		OAuth2UserInfo oAuth2UserInfo = tokenVerifyProcessor.verifyIdToken(providerType, idToken);
		Optional<User> user = userReader.findUserByEmail(oAuth2UserInfo.getEmail());

		return user.map(u -> {
			u.validateProvider(providerType);
			return new UserInfo.AuthProcess(u);
		}).orElseGet(() -> new UserInfo.AuthProcess(oAuth2UserInfo, providerType));
	}


	@Override
	public User findUserBy(String email) {
		return userReader.findUserByEmail(email).orElse(null);
	}

	@Override
	public User loadUserByEmail(String email) {
		return userReader.findUserByEmail(email)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
	}

	@Override
	public List<User> findUsersByEmails(List<String> emails) {
		return userReader.findUsersByEmails(emails);
	}

	@Override
	public void validateUserWithEmail(String email) {
		userReader.findUserByEmail(email)
				.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_EMAIL));
	}

	@Override
	public User store(User user) {
		return  userStore.store(user);
	}

}
