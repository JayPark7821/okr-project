package kr.objet.okrproject.domain.user.auth;

import kr.objet.okrproject.domain.user.enums.ProviderType;

public interface TokenVerifyProcessor {
	OAuth2UserInfo verifyIdToken(ProviderType provider, String token);
}
