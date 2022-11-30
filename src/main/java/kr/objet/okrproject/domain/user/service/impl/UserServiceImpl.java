package kr.objet.okrproject.domain.user.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;
	private final UserStore userStore;
	private final TokenVerifyProcessor tokenVerifyProcessor;

	@Override
	public UserInfo.Main getUserInfoFromIdToken(String provider, String idToken) {
		ProviderType providerType = ProviderType.of(provider);
		OAuth2UserInfo oAuth2UserInfo = tokenVerifyProcessor.verifyIdToken(providerType, idToken);
		Optional<User> savedUser = userReader.findUserByUserId(oAuth2UserInfo.getId());
		return savedUser.map(UserInfo.Main::new).orElse(null);
	}


	@Override
	public UserInfo.Main findUserInfoBy(String email) {
		Optional<User> user = userReader.findUserByEmail(email);
		return user.map(UserInfo.Main::new).orElse(null);
	}

	@Override
	public UserInfo.UserEntity loadUserByEmail(String email) {
		User user = userReader.findUserByEmail(email)
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.INVALID_USER_INFO));
		return new UserInfo.UserEntity(user);
	}

	public boolean isJoining(UserInfo.Main userInfo, String provider) {
		ProviderType providerType = ProviderType.of(provider);
		if (userInfo != null) {
			if (providerType != userInfo.getProviderType()) {
				throw new OkrApplicationException(ErrorCode.MISS_MATCH_PROVIDER,
					userInfo.getProviderType() + "(으)로 가입한 계정이 있습니다.");
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public UserInfo.Main store(User user) {
		User savedUser = userStore.store(user);
		return new UserInfo.Main(savedUser);
	}

}
