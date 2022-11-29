package kr.objet.okrproject.infrastructure.user.auth.verifier;

import java.util.List;

import org.springframework.stereotype.Component;

import kr.objet.okrproject.common.exception.ErrorCode;
import kr.objet.okrproject.common.exception.OkrApplicationException;
import kr.objet.okrproject.domain.user.ProviderType;
import kr.objet.okrproject.domain.user.auth.OAuth2UserInfo;
import kr.objet.okrproject.domain.user.auth.TokenVerifyProcessor;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenVerifyProcessorImpl implements TokenVerifyProcessor {

	private final List<TokenVerifier> tokenVerifierList;

	@Override
	public OAuth2UserInfo verifyIdToken(ProviderType provider, String token) {
		TokenVerifier tokenVerifier = routingApiCaller(provider);
		tokenVerifier.varifyIdToken(token);
		return null;
	}

	private TokenVerifier routingApiCaller(ProviderType provider) {
		return tokenVerifierList.stream()
			.filter(tokenVerifier -> tokenVerifier.support(provider))
			.findFirst()
			.orElseThrow(() -> new OkrApplicationException(ErrorCode.UNSUPPORTED_SOCIAL_TYPE));
	}

}
