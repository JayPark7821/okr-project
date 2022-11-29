package kr.objet.okrproject.infrastructure.user.auth.verifier;

import kr.objet.okrproject.domain.user.ProviderType;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;

public interface TokenVerifier {

	boolean support(ProviderType providerType);

	OAuth2UserInfo varifyIdToken(String token);
}
