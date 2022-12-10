package kr.objet.okrproject.domain.user.service.impl;

import org.springframework.stereotype.Service;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.user.User;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.auth.TokenVerifyProcessor;
import kr.objet.okrproject.domain.user.enums.ProviderType;
import kr.objet.okrproject.domain.user.service.UserReader;
import kr.objet.okrproject.domain.user.service.UserService;
import kr.objet.okrproject.domain.user.service.UserStore;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;
	private final UserStore userStore;
	private final TokenVerifyProcessor tokenVerifyProcessor;

	@Override
	public OAuth2UserInfo getUserInfoFromIdToken(ProviderType providerType, String idToken) {
		return tokenVerifyProcessor.verifyIdToken(providerType, idToken);
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
	public boolean isJoining(User user, ProviderType providerType) {
		if (user != null) {
			if (providerType != user.getProviderType()) {
				throw new OkrApplicationException(ErrorCode.MISS_MATCH_PROVIDER,
					user.getProviderType() + "(으)로 가입한 계정이 있습니다.");
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public User store(User user) {
		return  userStore.store(user);
	}

}
