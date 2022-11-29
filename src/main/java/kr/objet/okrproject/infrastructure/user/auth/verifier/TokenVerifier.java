package kr.objet.okrproject.infrastructure.user.auth.verifier;

import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.enums.ProviderType;

public interface TokenVerifier {

	boolean support(ProviderType providerType);

	OAuth2UserInfo varifyIdToken(String token);
}
