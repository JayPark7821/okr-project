package kr.objet.okrproject.domain.user.service.impl;

import javax.servlet.http.HttpServletRequest;

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
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserReader userReader;
	private final TokenVerifyProcessor tokenVerifyProcessor;

	@Override
	public UserInfo.Main loginWithSocialIdToken(HttpServletRequest request, String provider, String idToken) {
		ProviderType providerType = ProviderType.of(provider);
		OAuth2UserInfo oAuth2UserInfo = tokenVerifyProcessor.verifyIdToken(providerType, idToken);
		User savedUser = userReader.getUserByUserId(oAuth2UserInfo.getId());

		if (isUserJoinning(savedUser, providerType)) {
			return new UserInfo.Main(savedUser);
		} else {
			return null;
		}
	}

	public User loadUserByUsername(String username) {
		return userReader.getUserByUsername(username);
	}

	private boolean isUserJoinning(User userInfo, ProviderType providerType) {
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
}
