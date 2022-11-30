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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;
	private final UserStore userStore;
	private final TokenVerifyProcessor tokenVerifyProcessor;

	@Override
	public User getUserInfoFromIdToken(String provider, String idToken) {
		ProviderType providerType = ProviderType.of(provider);
		OAuth2UserInfo oAuth2UserInfo = tokenVerifyProcessor.verifyIdToken(providerType, idToken);
		return userReader.findUserByUserId(oAuth2UserInfo.getId()).orElse(null);
	}


	@Override
	public User findUserInfoBy(String email) {
		return userReader.findUserByEmail(email).orElse(null);
	}

	@Override
	public User loadUserByEmail(String email) {
		return userReader.findUserByEmail(email)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
	}

	@Override
	public boolean isJoining(User user, String provider) {
		ProviderType providerType = ProviderType.of(provider);
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
